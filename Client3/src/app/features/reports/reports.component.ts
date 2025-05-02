import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import html2pdf from 'html2pdf.js';
import { Router } from '@angular/router';
import { ReportService, ReportItem } from '../../core/services/report.service';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.css']
})
export class ReportsComponent implements OnInit {
  report: ReportItem[] = [];
  selectedDate: string = '';
  totalSum: number = 0;

  constructor(private reportService: ReportService, private router: Router) {}

  ngOnInit(): void {}

  loadReport(): void {
    if (!this.selectedDate) return;

    this.reportService.getReportByDate(this.selectedDate).subscribe(data => {
      this.report = data;
      this.totalSum = data.reduce((sum, item) => sum + item.total, 0);
    });
  }

  downloadPDF() {
    const element = document.getElementById('reportContent');
    const opt = {
      margin: 10,
      filename: `report_${this.selectedDate}.pdf`,
      image: { type: 'jpeg', quality: 0.98 },
      html2canvas: { scale: 2 },
      jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' }
    };

    if (element) {
      html2pdf().set(opt).from(element).save();
    }
  }

  goToMain() {
    this.router.navigate(['/main']);
  }
}
