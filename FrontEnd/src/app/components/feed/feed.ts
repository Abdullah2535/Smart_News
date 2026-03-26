import { Component, OnInit } from '@angular/core';
import { UserNews } from '../../models/news.model';
import { NewsCard } from '../news-card/news-card';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-feed',
  templateUrl: './feed.html',
  styleUrls: ['./feed.css'],
  imports: [NewsCard, CommonModule],
})
export class FeedComponent implements OnInit {
  newsList: UserNews[] = [];

  constructor() {}

  ngOnInit(): void {
    // 1. Read the state directly from the browser's history API
    const navigationState = window.history.state;

    console.log('🔍 Checking Navigation State:', navigationState);

    // 2. Check if 'feedData' was passed from the Preferences page
    if (navigationState && navigationState.feedData) {
      console.log('✅ 3. Data successfully caught in Feed:', navigationState.feedData);
      const rawData: UserNews[] = navigationState.feedData;

      // 3. Map the numeric IDs to your CSS colors
      this.newsList = rawData.map((article: UserNews) => ({
        ...article,
        vibe: this.getVibeFromSentiment(article.sentimentId),
      }));

      console.log('✅ Successfully loaded ' + this.newsList.length + ' articles from state!');
    } else {
      console.warn('⚠️ No data passed from preferences page. User might have refreshed.');
      // Note: If the user manually hits F5 to refresh the /feed page, the state is cleared.
      // Later on, you might want to add a fallback here to fetch the feed from the backend again!
    }
  }

  // Helper method: Assuming 1 = Positive, 2 = Neutral, 3 = Negative in your DB
  private getVibeFromSentiment(id: number): string {
    if (id === 1) return 'positive';
    if (id === 2) return 'neutral';
    if (id === 3) return 'negative';
    return 'neutral'; // Fallback
  }
}
