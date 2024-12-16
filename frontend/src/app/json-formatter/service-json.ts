import {
  ElementRef,
  Injectable
} from '@angular/core';
import {
  DomSanitizer,
  SafeHtml
} from '@angular/platform-browser';

@Injectable({
  providedIn: 'root'
})
export class JsonFormatterService {
  constructor(
    private sanitizer: DomSanitizer,
  ) {}

  parseAndFormatJSON(jsonInput: string, isMinify: boolean): { formattedJson: SafeHtml, isError: boolean, errorMessage: string } {
    let formattedJson: SafeHtml = '';
    let isError = false;
    let errorMessage = '';

    try {
      const parsedJson = JSON.parse(jsonInput);
      let formatted = JSON.stringify(parsedJson, null, isMinify ? 0 : 2);
      formattedJson = this.sanitizer.bypassSecurityTrustHtml(formatted);
    } catch (error) {
      errorMessage = `Invalid JSON: ${(error as Error).message}`;
      isError = true;
    }
    return { formattedJson, isError, errorMessage };
  }

  validateJSON(jsonInput: string): { isError: boolean, errorMessage: string } {
    let isError = false;
    let errorMessage = '';

    try {
      JSON.parse(jsonInput);
    } catch (error) {
      isError = true;
      errorMessage = `Invalid JSON: ${(error as Error).message}`;
    }
    return { isError, errorMessage };
  }

  adjustHeightInput(jsonInputTextarea: ElementRef<HTMLTextAreaElement>, lineNumbers: ElementRef<HTMLElement>): void {
    const textarea = jsonInputTextarea.nativeElement;
    const divNumbers = lineNumbers.nativeElement;

    textarea.style.height = 'auto';
    textarea.style.height = `${textarea.scrollHeight}px`;
    divNumbers.style.height = 'auto';
    divNumbers.style.height = `${textarea.scrollHeight}px`;
  }

  adjustHeightOutput(jsonOutputDiv: ElementRef<HTMLElement>, lineNumbersOutput: ElementRef<HTMLElement>): void {
    const outputDiv = jsonOutputDiv.nativeElement;
    const divNumbers = lineNumbersOutput.nativeElement;

    divNumbers.style.height = 'auto';
    divNumbers.style.height = `${outputDiv.scrollHeight}px`;
  }
}
