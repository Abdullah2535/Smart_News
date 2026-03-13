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

    this.categoryService.getPreferences().subscribe((res) => {
      this.sentiments = res.sentiments;
      this.categories = res.categories;

      // If nothing in localStorage, select all by default
      if (!storedSentiments) this.selectedSentiments = this.sentiments.map((s) => s.id);
      if (!storedCategories) this.selectedCategories = this.categories.map((c) => c.id);
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
    localStorage.setItem('selectedSentiments', JSON.stringify(this.selectedSentiments));
    localStorage.setItem('selectedCategories', JSON.stringify(this.selectedCategories));

    // Navigate to next component
    this.router.navigate(['/news']);
  }

  isSentimentSelected(id: number): boolean {
    return this.selectedSentiments.includes(id);
  }

  isCategorySelected(id: number): boolean {
    return this.selectedCategories.includes(id);
  }
}
