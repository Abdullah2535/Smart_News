import asyncio
import json
import aiohttp 
from urllib.parse import unquote 
from crawl4ai import AsyncWebCrawler, CrawlerRunConfig, CacheMode
from crawl4ai.extraction_strategy import JsonCssExtractionStrategy

async def main():
    # 1. Define the Schema
    schema = {
        "name": "Nabd News",
        "baseSelector": "div.regular-story", 
        "fields": [
            {"name": "id", "selector": "span.hidden", "type": "attribute", "attribute": "id"},
            {"name": "headline", "selector": ".media-heading a", "type": "text"},
            {"name": "articleUrl", "selector": ".media-heading a", "type": "attribute", "attribute": "href"},
            {"name": "source", "selector": "span.media-date", "type": "text"},
            {"name": "thumbnail", "selector": "img.media-object", "type": "attribute", "attribute": "data-src"}
        ]
    }

    extraction_strategy = JsonCssExtractionStrategy(schema)

    # 2. Configuration
    config = CrawlerRunConfig(
        extraction_strategy=extraction_strategy,
        cache_mode=CacheMode.BYPASS,
        # Wait for at least 35 items
        wait_for="js:() => document.querySelectorAll('div.regular-story').length >= 10",
        # Scroll logic
        js_code="""
            (async () => {
                let count = 0;
                while (document.querySelectorAll('div.regular-story').length < 35 && count < 10) {
                    window.scrollTo(0, document.body.scrollHeight);
                    await new Promise(resolve => setTimeout(resolve, 1500));
                    count++;
                }
            })();
        """,
        page_timeout=100000 
    )

    urls = [
        "https://nabd.com/category/3-7d05a7/%D9%85%D8%B5%D8%B1",       
        "https://nabd.com/category/1-1388b4/%D8%A7%D9%84%D8%B9%D8%A7%D9%84%D9%85", 
        "https://nabd.com/category/27-d6633d/%D8%A5%D9%82%D8%AA%D8%B5%D8%A7%D8%AF",   
        "https://nabd.com/category/18-a2e886/%D8%B1%D9%8A%D8%A7%D8%B6%D8%A9" 
    ]

    # Map readable Arabic keys to IDs
    category_id_map = {
        "مصر": 4,
        "العالم": 5,
        "إقتصاد": 6,
        "رياضة": 7
    }

    final_payload = []

    # 3. Run the Crawler
    async with AsyncWebCrawler() as crawler:
        results = await crawler.arun_many(urls=urls, config=config)

        for result in results:
            # Decode the URL fragment (%D9%85... -> مصر)
            raw_fragment = result.url.split('/')[-1]
            url_fragment = unquote(raw_fragment) 
            
            # Get the Integer ID (Default to 0 if not found)
            category_id = category_id_map.get(url_fragment, 0)

            if result.success:
                data = json.loads(result.extracted_content)
                
                # Process only the first 35 items
                for item in data[:35]:
                    item['categoryId'] = category_id
                    final_payload.append(item)
                
                print(f" Processed Category: {url_fragment} (ID: {category_id}) - Added {len(data[:35])} items")
            else:
                print(f" Failed URL {result.url} - Error: {result.error_message}")

    # 4. Save Locally for Debugging (This runs regardless of POST success)
    local_filename = 'nabd_debug_data.json'
    try:
        with open(local_filename, 'w', encoding='utf-8') as f:
            json.dump(final_payload, f, indent=2, ensure_ascii=False)
        print(f"\n Saved local copy to '{local_filename}'")
    except Exception as e:
        print(f" Failed to save local file: {e}")

   # 5. Send POST Request and Trigger AI
    target_url = "http://localhost:8080/news"
    ai_trigger_url = "http://localhost:8080/news/ai" # The new AI endpoint
    
    print(f"📤 Sending {len(final_payload)} items to {target_url}...")

    try:
        async with aiohttp.ClientSession() as session:
            # STEP 1: Save the raw news to MySQL
            async with session.post(target_url, json=final_payload) as response:
                if response.status in [200, 201]:
                    print("✅ POST Success: Raw data saved successfully.")
                    
                    # STEP 2: Prepare the ID list for the AI pipeline
                    # We extract only the 'id' field and ensure it's an integer for Spring Boot
                    ai_payload = [{"id": int(item["id"])} for item in final_payload if "id" in item and str(item["id"]).isdigit()]
                    
                    if not ai_payload:
                        print("⚠️ No valid IDs found to send to the AI pipeline.")
                        return

                    print(f"🧠 Triggering Java AI Pipeline for {len(ai_payload)} articles...")
                    
                    # STEP 3: Fire the PUT request to start Qwen and Nomic
                    async with session.put(ai_trigger_url, json=ai_payload) as ai_response:
                        if ai_response.status in [200, 201]:
                            print(" AI Pipeline Started Successfully!")
                            print(f"Backend Response: {await ai_response.text()}")
                    

                else:
                    print(f" POST Failed: Status Code {response.status}")
                    response_text = await response.text()
                    print(f"Response: {response_text}")

    except Exception as e:
        print(f"❌ Connection Error: Could not connect to backend")
        print(f"Details: {e}")

if __name__ == "__main__":
    asyncio.run(main())