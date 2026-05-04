# Smart_News
This project was developed to provide a sophisticated, AI-driven solution to the challenges of
information retrieval in the Arabic news sector. By integrating a high-performance Spring Boot
backend with a modular Angular frontend, the system successfully bridges the gap between raw
data ingestion and a personalized user experience. The core technical achievement lies in the local
hosting of Large Language Models (LLMs) via Ollama, allowing for high-accuracy sentiment
analysis and semantic embedding without the data privacy concerns or costs associated with cloud-
based AI providers.

Architecture and Implementation Summary
Throughout the development lifecycle, several critical technical objectives were met,
demonstrating the feasibility of integrating advanced NLP models on consumer-grade hardware.

Intelligent Ingestion and Orchestration
The project began with the development of a robust Aggregating module using Crawl4AI. This
module was engineered to scrape and sanitize content from diverse Arabic sources, such as Al
Arabiya, handling the "messy" nature of web data through whitespace stripping, HTML entity
decoding, and Arabic text normalization. To handle the computational demands of AI processing,
the system utilizes a "Sweep" orchestration pattern. This self-healing architecture identifies
articles in the MySQL database with null sentiment identifiers and processes them in a sequential
pipeline. The backend manages this workflow by coordinating with local AI services to extract
sentiment via Qwen 2.5 and generate 768-dimensional semantic embeddings via Nomic.

The 769th Dimension Fusion Logic
A significant technical milestone of this project is the 769th dimension fusion. Instead of treating
sentiment and semantic relevance as separate entities, the implementation concatenates the 768-
dimension vector with a normalized sentiment. This mathematical fusion allows the Cosine
Similarity algorithm, implemented within the Java application layer, to perform multi-criteria
ranking in a single pass. By mapping articles into this hybrid 769-dimensional space, the system
ensures that the news feed is not only topically relevant but also emotionally aligned with the user's
preference logs.

Backend Optimization and Security
To support these high-dimensional operations, the backend was optimized for speed and stability.
Rather than utilizing standard JPA saves for every article—which would create I/O bottlenecks—
the system utilizes JdbcTemplate batching to process hundreds of news records simultaneously.
Vectors persisted as JSON strings within MySQL, and Spring Data JPA Projections are used to
fetch only essential data during the sweep, minimizing memory consumption on the host machine.
Security was addressed through a stateless JWT (JSON Web Token) architecture. The
implementation of a dual-token strategy (Access and Refresh tokens) ensures that user preference
logs—currently stored as structured strings of category and sentiment IDs—remain protected. The
backend enforces cryptographic integrity via parseSignedClaims, ensuring that any unauthorized
modification of the user’s token results in immediate rejection.

Frontend and UX Implementation
The Angular frontend was developed to translate complex AI data into an intuitive dashboard.
Following a modular, component-based design, the presentation layer separates UI concerns from
business logic.
• Modular Components: Components like feed and news-card were designed to render AI-
derived metadata, such as credibility scores and sentiment tags, in a clean card-based
layout.
• Security Interceptors: An HTTP Interceptor was implemented to automatically append
JWTs to all outgoing requests, ensuring seamless authentication between the client and the
Spring Boot API.
• Localization: The interface was built with native Right-to-Left (RTL) support, specifically
tailored for the Arabic-speaking audience.

Experimental Results and Validation
The project was rigorously tested against a dataset of 425 news articles. The experimental results
confirm that the chosen architecture is highly effective:
• Sentiment Accuracy: The Qwen 2.5 (3B) model demonstrated a 92% accuracy rate in
identifying the emotional tone of Arabic headlines, correctly partitioning the dataset into
positive, negative, and neutral clusters.
• Credibility Assessment: The system successfully flagged 85% of sensationalist
"clickbait" titles by assigning low credibility scores based on linguistic markers like
"urgent" or excessive punctuation.
• System Throughput: The batch processing logic allowed the system to process the entire
test set, including scraping, sentiment extraction, and vectorization—in under three
minutes, demonstrating its readiness for production-level news aggregation.

<img width="352" height="525" alt="login" src="https://github.com/user-attachments/assets/5abdaf6a-58a2-4fab-aa73-9890b864e363" />
                                                        Login page

<img width="529" height="821" alt="image" src="https://github.com/user-attachments/assets/2cc2cda3-1470-49db-9d7e-443eb5d1af27" />
                                                        SignUp page


<img width="866" height="947" alt="image" src="https://github.com/user-attachments/assets/048b9e86-1756-4de0-82fc-def1c8dc9f05" />
                                                      Navigation hub page

<img width="369" height="849" alt="preferences" src="https://github.com/user-attachments/assets/d54f823f-35fd-4bd0-83e2-f7537859c8bb" />
                                                     Preferences page

<img width="779" height="960" alt="image" src="https://github.com/user-attachments/assets/0e360a51-acf7-4feb-b840-955cb0165eec" />
                                                     Feed page
                                                       


