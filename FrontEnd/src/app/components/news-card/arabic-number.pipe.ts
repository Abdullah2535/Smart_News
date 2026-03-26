import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'arabicNumber',
  standalone: true, // ⬅️ Makes it easy to use anywhere!
})
export class ArabicNumberPipe implements PipeTransform {
  transform(value: string | null | undefined): string {
    if (!value) return '';

    // The Eastern Arabic numerals array
    const arabicNumbers = ['٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'];

    // Finds any standard number (0-9) and replaces it with the Arabic equivalent
    return value.replace(/[0-9]/g, (char) => arabicNumbers[parseInt(char, 10)]);
  }
}
