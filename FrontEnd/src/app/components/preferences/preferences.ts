import { Component, OnInit } from '@angular/core';
import { CategoryService } from '../../services/category.service';
import { IdValuePair, PreferencesResponse } from '../../models/category.model';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-preferences',
  templateUrl: './preferences.html',
  styleUrls: ['./preferences.css'],
  imports: [CommonModule], // <-- this fixes the warning
})
export class PreferencesComponent implements OnInit {
  sentiments: IdValuePair[] = [];
  categories: IdValuePair[] = [];

  selectedSentiments: number[] = [];
  selectedCategories: number[] = [];

  constructor(
    private categoryService: CategoryService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    // Load selected IDs from localStorage first
    const storedSentiments = localStorage.getItem('selectedSentiments');
    const storedCategories = localStorage.getItem('selectedCategories');

    this.selectedSentiments = storedSentiments ? JSON.parse(storedSentiments) : [];
    this.selectedCategories = storedCategories ? JSON.parse(storedCategories) : [];

    // 3. important detail HERE
    // Because the service returns an Observable<PreferencesResponse>,
    // res.sentiments' is automatically recognized as an array of IdValuePair!
    this.categoryService.getPreferences().subscribe((res: PreferencesResponse) => {
      this.sentiments = res.sentiments;
      this.categories = res.categories;

      //leaving the buttons unselected
      if (!storedSentiments) this.selectedSentiments = [];
      if (!storedCategories) this.selectedCategories = [];
    });
  }

  toggleSentiment(id: number) {
    const index = this.selectedSentiments.indexOf(id);
    if (index > -1) this.selectedSentiments.splice(index, 1);
    else this.selectedSentiments.push(id);
  }

  toggleCategory(id: number) {
    const index = this.selectedCategories.indexOf(id);
    if (index > -1) this.selectedCategories.splice(index, 1);
    else this.selectedCategories.push(id);
  }

  savePreferences() {
    // 1. Save locally for fast frontend rendering
    localStorage.setItem('selectedSentiments', JSON.stringify(this.selectedSentiments));
    localStorage.setItem('selectedCategories', JSON.stringify(this.selectedCategories));

    // 2. Send to Spring Boot Database!
    console.log('🚀 1. Button clicked, starting request...');

    this.categoryService
      .saveUserPreferences(this.selectedSentiments, this.selectedCategories)
      .subscribe({
        next: (newsResponse) => {
          // We ONLY navigate inside this block
          if (newsResponse && newsResponse.length > 0) {
            console.log('✅ 2. Data received, NOW navigating:', newsResponse);

            this.router.navigateByUrl('/feed', {
              state: { feedData: newsResponse },
            });
          } else {
            console.warn('⚠️ Backend returned empty news list.');
          }
        },
        error: (err) => {
          console.error('❌ 2. Backend error:', err);
        },
      });
  }

  isSentimentSelected(id: number): boolean {
    return this.selectedSentiments.includes(id);
  }

  isCategorySelected(id: number): boolean {
    return this.selectedCategories.includes(id);
  }

  // --- NEW METHOD FOR CSS COLORS ---
  getVibe(value: string): string {
    if (value === 'إيجابي') return 'positive';
    if (value === 'محايد') return 'neutral';
    if (value === 'سلبي') return 'negative';
    return 'neutral';
  }
}
