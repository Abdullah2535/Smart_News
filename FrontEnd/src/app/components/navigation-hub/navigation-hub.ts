import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms'; // ⬅️ Required for handling inputs
import { NewsService } from '../../services/News.service';
import { UserNews } from '../../models/news.model';

@Component({
  selector: 'app-navigation-hub',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule], // ⬅️ Add FormsModule here
  templateUrl: './navigation-hub.html',
  styleUrls: ['./navigation-hub.css'], // (Leave empty or keep your previous styles)
})
export class NavigationHub {
  // 1. These variables will hold what the user types!
  searchKeyword: string = '';
  articleLimit: number = 10;
  articles: UserNews[] = [];
  isLoading: boolean = false;

  constructor(
    private router: Router,
    private newsService: NewsService,
  ) {}

  onSearchClick() {
    this.isLoading = true;
    // ✅ The .subscribe() tells Angular to actually fire the request
    this.newsService.searchNews(this.searchKeyword, this.articleLimit).subscribe({
      next: (data) => {
        console.log('Search data received!', data);
        this.articles = data; // Update  UI with the results
        // 3. Stop the spinner (though routing happens immediately after)
        this.isLoading = false;

        // 3. Route to the feed and pass the data along in the state

        this.router.navigate(['/feed'], { state: { searchData: data } });
      },
      error: (err) => {
        console.error('Search failed to complete', err);
        // 4. Stop the spinner if there's an error, so the user can try again
        this.isLoading = false;
      },
    });
  }
}
