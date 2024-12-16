import {ChangeDetectorRef, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import { FormsModule } from "@angular/forms";
import { NgClass, NgForOf, NgIf } from "@angular/common";
import hljs from 'highlight.js';
import {SafeHtml} from "@angular/platform-browser";
import {JsonFormatterService} from "./service-json";

@Component({
  selector: 'app-json-formatter',
  standalone: true,
  imports: [
    FormsModule,
    NgClass,
    NgIf,
    NgForOf
  ],
  templateUrl: './json-formatter.component.html',
  styleUrls: ['./json-formatter.component.css']
})
export class JsonFormatterComponent implements OnInit {
  jsonInput = '';
  formattedJson = '';
  isError = false;
  errorMessage = '';
  inputLineNumbers: number[] = [];
  outputLineNumbers: number[] = [];
  isMinify = false;

  @ViewChild('jsonInputTextarea') jsonInputTextarea!: ElementRef<HTMLTextAreaElement>;
  @ViewChild('lineNumbersInput') lineNumbersInput!: ElementRef<HTMLElement>;
  @ViewChild('lineNumbersOutput') lineNumbersOutput!: ElementRef<HTMLElement>;
  @ViewChild('jsonOutputDiv') jsonOutputDiv!: ElementRef<HTMLElement>;

  constructor(private jsonFormatterService: JsonFormatterService,
              private cdr: ChangeDetectorRef) {
  }

  onInput(): void {
    this.updateLineNumbers();
    this.adjustHeightInput();
  }

  ngOnInit(): void {
    this.cdr.detectChanges();
    this.adjustHeightOutput();
  }

  private parseAndFormatJSON(isMinify: boolean): void {
    const result = this.jsonFormatterService.parseAndFormatJSON(this.jsonInput, isMinify);
    this.formattedJson = <string>result.formattedJson;
    this.isError = result.isError;
    this.errorMessage = result.errorMessage;
    this.updateLineNumbers();
    this.cdr.detectChanges();
    this.adjustHeightOutput();
  }

  formatJSON(): void {
    this.isMinify = false;
    this.parseAndFormatJSON(this.isMinify);
  }

  minifyJSON(): void {
    this.isMinify = true;
    this.parseAndFormatJSON(this.isMinify);
  }

  validateJSON(): void {
    const result = this.jsonFormatterService.validateJSON(this.jsonInput);
    this.isError = result.isError;
    this.errorMessage = result.errorMessage;
  }

  updateLineNumbers(): void {
    this.inputLineNumbers = Array.from({length: (this.jsonInput.match(/\n/g) || []).length + 1}, (_, i) => i + 1);
    this.outputLineNumbers = Array.from({length: this.formattedJson.toString().split('\n').length}, (_, i) => i + 1);
  }

  adjustHeightInput(): void {
    this.jsonFormatterService.adjustHeightInput(this.jsonInputTextarea, this.lineNumbersInput);
  }

  adjustHeightOutput(): void {
    this.jsonFormatterService.adjustHeightOutput(this.jsonOutputDiv, this.lineNumbersOutput);
  }
  ngAfterViewInit(): void {
    this.attachButtonClickHandlers();
  }

  attachButtonClickHandlers(): void {
    const outputDiv = this.jsonOutputDiv.nativeElement;
    const braceButtons = outputDiv.querySelectorAll('button.brace-button');

    braceButtons.forEach((button) => {
      button.addEventListener('click', this.handleBraceClick.bind(this));
    });
  }

  handleBraceClick(event: Event): void {
    const target = event.target as HTMLElement;
    console.log('Brace button clicked near an open brace');
    // Add specific logic for brace button click here
  }

}

