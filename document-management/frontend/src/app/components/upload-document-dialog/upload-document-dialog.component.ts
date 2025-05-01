import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { DocumentService } from '../../services/document.service';

@Component({
  selector: 'app-upload-document-dialog',
  template: `
    <h2 mat-dialog-title>Upload Document</h2>
    <mat-dialog-content>
      <form [formGroup]="uploadForm" (ngSubmit)="onSubmit()">
        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Title</mat-label>
          <input matInput formControlName="title" placeholder="Enter document title">
          <mat-error *ngIf="uploadForm.get('title')?.hasError('required')">
            Title is required
          </mat-error>
        </mat-form-field>

        <div class="file-upload-container">
          <input type="file" #fileInput (change)="onFileSelected($event)" style="display: none">
          <button mat-raised-button type="button" (click)="fileInput.click()">
            Choose File
          </button>
          <span class="file-name" *ngIf="selectedFile">
            {{selectedFile.name}}
          </span>
        </div>
      </form>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button (click)="onCancel()">Cancel</button>
      <button mat-raised-button color="primary" 
              [disabled]="!uploadForm.valid || !selectedFile"
              (click)="onSubmit()">
        Upload
      </button>
    </mat-dialog-actions>
  `,
  styles: [`
    .full-width {
      width: 100%;
      margin-bottom: 16px;
    }
    .file-upload-container {
      display: flex;
      align-items: center;
      gap: 16px;
      margin-bottom: 16px;
    }
    .file-name {
      flex: 1;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  `]
})
export class UploadDocumentDialogComponent {
  uploadForm: FormGroup;
  selectedFile: File | null = null;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<UploadDocumentDialogComponent>,
    private documentService: DocumentService
  ) {
    this.uploadForm = this.fb.group({
      title: ['', Validators.required]
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
    }
  }

  onSubmit(): void {
    if (this.uploadForm.valid && this.selectedFile) {
      this.documentService.uploadDocument(this.selectedFile, this.uploadForm.value.title)
        .subscribe({
          next: () => {
            this.dialogRef.close(true);
          },
          error: (error) => {
            console.error('Error uploading document:', error);
          }
        });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
} 