import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { ViewChild } from '@angular/core';
import { DocumentService, Document } from '../../services/document.service';
import { MatDialog } from '@angular/material/dialog';
import { UploadDocumentDialogComponent } from '../upload-document-dialog/upload-document-dialog.component';

@Component({
  selector: 'app-document-list',
  template: `
    <div class="document-list-container">
      <div class="header">
        <h1>Documents</h1>
        <button mat-raised-button color="primary" (click)="openUploadDialog()">
          Upload Document
        </button>
      </div>

      <mat-form-field appearance="outline" class="search-field">
        <mat-label>Search</mat-label>
        <input matInput (keyup)="applyFilter($event)" placeholder="Search documents">
        <mat-icon matSuffix>search</mat-icon>
      </mat-form-field>

      <div class="table-container">
        <table mat-table [dataSource]="dataSource" class="mat-elevation-z8">
          <ng-container matColumnDef="title">
            <th mat-header-cell *matHeaderCellDef>Title</th>
            <td mat-cell *matCellDef="let document">{{document.title}}</td>
          </ng-container>

          <ng-container matColumnDef="fileType">
            <th mat-header-cell *matHeaderCellDef>Type</th>
            <td mat-cell *matCellDef="let document">{{document.fileType}}</td>
          </ng-container>

          <ng-container matColumnDef="uploadedAt">
            <th mat-header-cell *matHeaderCellDef>Uploaded At</th>
            <td mat-cell *matCellDef="let document">{{document.uploadedAt | date}}</td>
          </ng-container>

          <ng-container matColumnDef="actions">
            <th mat-header-cell *matHeaderCellDef>Actions</th>
            <td mat-cell *matCellDef="let document">
              <button mat-icon-button color="warn" (click)="deleteDocument(document.id)">
                <mat-icon>delete</mat-icon>
              </button>
            </td>
          </ng-container>

          <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
          <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
        </table>

        <mat-paginator [pageSizeOptions]="[5, 10, 25, 100]" aria-label="Select page of documents">
        </mat-paginator>
      </div>
    </div>
  `,
  styles: [`
    .document-list-container {
      padding: 20px;
    }
    .header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
    }
    .search-field {
      width: 100%;
      margin-bottom: 20px;
    }
    .table-container {
      overflow-x: auto;
    }
    table {
      width: 100%;
    }
  `]
})
export class DocumentListComponent implements OnInit {
  displayedColumns: string[] = ['title', 'fileType', 'uploadedAt', 'actions'];
  dataSource = new MatTableDataSource<Document>();
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private documentService: DocumentService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loadDocuments();
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
  }

  loadDocuments(): void {
    this.documentService.getDocuments().subscribe({
      next: (response) => {
        this.dataSource.data = response.content;
      },
      error: (error) => {
        console.error('Error loading documents:', error);
      }
    });
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  openUploadDialog(): void {
    const dialogRef = this.dialog.open(UploadDocumentDialogComponent, {
      width: '400px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadDocuments();
      }
    });
  }

  deleteDocument(id: number): void {
    if (confirm('Are you sure you want to delete this document?')) {
      this.documentService.deleteDocument(id).subscribe({
        next: () => {
          this.loadDocuments();
        },
        error: (error) => {
          console.error('Error deleting document:', error);
        }
      });
    }
  }
} 