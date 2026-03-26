export interface UserNews {
  headline: string;
  articleUrl: string;
  source: string;
  thumbnail: string;
  publishedAt: string;
  sentimentId: number;

  //  optional frontend-only property to map the
  // sentimentId (1, 2, 3) to your CSS colors ('positive', 'neutral', 'negative')
  vibe?: string;
}
