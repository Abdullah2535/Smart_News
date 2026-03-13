export interface IdValuePair {
  id: number;
  value: string;
}

export interface PreferencesResponse {
  sentiments: IdValuePair[];
  categories: IdValuePair[];
}
