import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Document {
  id: number;
  title: string;
  content: string;
  fileType: string;
  filePath: string;
  uploadedBy: {
    id: number;
    email: string;
  };
  uploadedAt: string;
  lastModifiedAt: string;
  isDeleted: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class DocumentService {
  constructor(private http: HttpClient) {}

  uploadDocument(file: File, title: string): Observable<Document> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('title', title);
    
    return this.http.post<Document>(`${environment.apiUrl}/documents/upload`, formData);
  }

  getDocuments(page: number = 0, size: number = 10): Observable<any> {
    return this.http.get(`${environment.apiUrl}/documents`, {
      params: {
        page: page.toString(),
        size: size.toString()
      }
    });
  }

  searchDocuments(keyword: string, page: number = 0, size: number = 10): Observable<any> {
    return this.http.get(`${environment.apiUrl}/documents/search`, {
      params: {
        keyword,
        page: page.toString(),
        size: size.toString()
      }
    });
  }

  deleteDocument(id: number): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/documents/${id}`);
  }

  getAllDocuments(): Observable<Document[]> {
    return this.http.get<Document[]>(`${environment.apiUrl}/documents/all`);
  }
} 