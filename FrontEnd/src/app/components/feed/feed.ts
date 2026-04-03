import { Component, OnInit } from '@angular/core';
import { UserNews } from '../../models/news.model';
import { NewsCard } from '../news-card/news-card';
import { CommonModule } from '@angular/common';
import { NewsService } from '../../services/News.service';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-feed',
  templateUrl: './feed.html',
  styleUrls: ['./feed.css'],
  imports: [NewsCard, CommonModule,RouterModule],
})
export class FeedComponent implements OnInit {
  newsList: UserNews[] = [];

  // Optional: Variables to show a "Search Results for..." header in  HTML later
  isShowingSearchResults: boolean = false;
  currentSearchKeyword: string = '';

  constructor(
    private newsService: NewsService,
    private router: Router,
  ) {}

  ngOnInit(): void {
  const navigationState = history.state;
  console.log('🔍 Checking Navigation State:', navigationState);

  //  SCENARIO A: The Custom Search Route (Data passed from NavigationHub)
  // We check for searchData because that's what NavigationHub sent!
  if (navigationState && navigationState.searchData) {
    console.log('✅ Caught the search data from NavigationHub!', navigationState.searchData);
    
    this.isShowingSearchResults = true;
    // Optional: if you also passed the keyword from the Hub, you can set it here
    this.currentSearchKeyword = navigationState.keyword || 'Search Results'; 

    // Skip the backend! Just process the data we already have.
    this.processNews(navigationState.searchData);
  }

  //  SCENARIO B: Fast route from Preferences (The Backpack is full)
  // Notice the "else if" here! This prevents Scenario C from firing by accident.
  else if (navigationState && navigationState.feedData && navigationState.feedData.length > 0) {
    console.log('✅ Data successfully caught in Feed:', navigationState.feedData);
    
    this.isShowingSearchResults = false;
    this.processNews(navigationState.feedData);
  }

  //  SCENARIO C: The Cold Start / Fallback Route (The Backpack is empty)
  else {
    console.warn('📡 No state data found. Fetching fresh from database...');
    this.isShowingSearchResults = false;

    // ⬅️ 4. The Fallback logic using your NewsService!
    this.newsService.getPersonalizedFeed().subscribe({
      next: (databaseNews) => {
        // If the array is empty, they have no preferences. Kick them to setup!
        if (!databaseNews || databaseNews.length === 0) {
          console.warn('⚠️ No preferences found for user! Redirecting to setup...');
          this.router.navigate(['/preferences']);
        } else {
          // Otherwise, load the news!
          console.log('✅ Successfully loaded news from backend!');
          this.processNews(databaseNews);
        }
      },
      error: (err) => {
        console.error('❌ Failed to fetch fallback news:', err);
      },
    });
  }
}

// Your processNews helper remains exactly the same!
private processNews(rawData: UserNews[]): void {
  this.newsList = rawData.map((article: UserNews) => ({
    ...article,
    vibe: this.getVibeFromSentiment(article.sentimentId),
  }));
  console.log('✅ Successfully loaded ' + this.newsList.length + ' articles!');
}

  // Helper method: Assuming 1 = Positive, 2 = Neutral, 3 = Negative in your DB
  private getVibeFromSentiment(id: number): string {
    if (id === 1) return 'positive';
    if (id === 2) return 'neutral';
    if (id === 3) return 'negative';
    return 'neutral'; // Fallback
  }
}
