import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common'; // ⬅️ Required for the Date Pipe!
import { UserNews } from '../../models/news.model';

//  Import the locale tools and the Egyptian Arabic data
import { registerLocaleData } from '@angular/common';
import localeArSa from '@angular/common/locales/ar-SA';
import { ArabicNumberPipe } from './arabic-number.pipe';
//   Register it so the DatePipe can use it
registerLocaleData(localeArSa);

@Component({
  selector: 'app-news-card',
  standalone: true,
  imports: [CommonModule, ArabicNumberPipe], // ⬅️ Add it here
  templateUrl: './news-card.html',
  styleUrls: ['./news-card.css'],
})
export class NewsCard {
  @Input() article!: UserNews;

  // This method checks if an image exists. If not, it assigns a default.
  getThumbnail(): string {
    if (this.article.thumbnail && this.article.thumbnail.trim() !== '') {
      return this.article.thumbnail;
    }

    // Fallback: If you add 'category' to your DTO later, you can do:
    // if (this.article.category === 'رياضة') return 'assets/sports-default.jpg';

    // For now, return a generic placeholder image from your assets folder!
    return 'default-news.webp';
  }

  // Backup safety net: If the URL is broken and fails to load in the browser
  handleImageError(event: any) {
    event.target.src = 'default-news.webp';
  }
}
