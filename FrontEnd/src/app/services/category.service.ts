import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { PreferencesResponse } from '../models/category.model';

@Injectable({
  providedIn: 'root',
})
export class CategoryService {
  constructor() {}

  // Simulate API call
  getPreferences(): Observable<PreferencesResponse> {
    const body: PreferencesResponse = {
      sentiments: [
        { id: 1, value: 'إيجابي' },
        { id: 2, value: 'محايد' },
        { id: 3, value: 'سلبي' },
      ],
      categories: [
        { id: 6, value: 'إقتصاد' },
        { id: 5, value: 'العالم' },
        { id: 7, value: 'رياضة' },
        { id: 4, value: 'مصر' },
      ],
    };
    return of(body);
  }
}
