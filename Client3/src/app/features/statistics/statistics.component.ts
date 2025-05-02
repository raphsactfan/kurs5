import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import jsPDF from 'jspdf';
import { StatisticsService } from '../../core/services/statistics.service';
import { Chart, ChartConfiguration, registerables } from 'chart.js';


Chart.register(...registerables);

@Component({
  selector: 'app-admin-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css'],
  standalone: true
})
export class StatisticsComponent implements OnInit {

  constructor(private statsService: StatisticsService, private router: Router) {}

  ngOnInit(): void {
    this.loadCategoryStats();
    this.loadSupplierStats();
    this.loadTopUsers();
    this.loadDailySales();
  }

  loadCategoryStats() {
    this.statsService.getCategoryStats().subscribe(data => {
      const labels = Object.keys(data);
      const values = Object.values(data);
      new Chart('categoryChart', {
        type: 'pie' as const,
        data: {
          labels,
          datasets: [{
            data: values
          }]
        }
      });
    });
  }

  loadSupplierStats() {
    this.statsService.getSupplierStats().subscribe(data => {
      const labels = Object.keys(data);
      const values = Object.values(data);
      new Chart('supplierChart', {
        type: 'pie' as const,
        data: {
          labels,
          datasets: [{
            data: values
          }]
        }
      });
    });
  }

  loadTopUsers() {
    this.statsService.getTopUsers().subscribe(data => {
      const labels = Object.keys(data);
      const values = Object.values(data);
      new Chart('topUsersChart', {
        type: 'bar' as const,
        data: {
          labels,
          datasets: [{
            label: 'Количество заказов',
            data: values,
            backgroundColor: '#2ecc71'
          }]
        },
        options: {
          plugins: {
            legend: { display: false }
          },
          scales: {
            y: {
              beginAtZero: true,
              ticks: { stepSize: 1 }
            }
          }
        }
      });
    });
  }

  loadDailySales() {
    this.statsService.getDailySales().subscribe(data => {
      const labels = Object.keys(data);
      const values = Object.values(data);

      const config = {
        type: 'line',
        data: {
          labels: labels,
          datasets: [{
            label: 'Продажи по дням',
            data: values,
            fill: false,
            borderColor: '#e67e22',
            tension: 0.3
          }]
        },
        options: {
          scales: {
            y: {
              beginAtZero: true,
              ticks: {
                stepSize: 1
              }
            }
          }
        }
      } as ChartConfiguration<'line', number[], string>; // <<< ВАЖНО

      new Chart('dailySalesChart', config);
    });
  }


  downloadChartAsPDF(canvasId: string, title: string) {
    const canvas: any = document.getElementById(canvasId);
    const imageData = canvas.toDataURL('image/png');

    const pdfWidth = canvas.width;
    const pdfHeight = canvas.height + 50;

    const pdf = new jsPDF({
      orientation: 'landscape',
      unit: 'px',
      format: [pdfWidth, pdfHeight]
    });

    pdf.setFontSize(16);

    const latinTitle =
      canvasId === 'categoryChart'
        ? 'Products by Category'
        : canvasId === 'supplierChart'
          ? 'Products by Supplier'
          : canvasId === 'topUsersChart'
            ? 'Top Users by Orders'
            : canvasId === 'dailySalesChart'
              ? 'Sales by Date'
              : 'Chart';

    const textWidth = pdf.getTextWidth(latinTitle);
    const x = (pdfWidth - textWidth) / 2;

    pdf.text(latinTitle, x, 30);
    pdf.addImage(imageData, 'PNG', 20, 50, canvas.width - 40, canvas.height - 60);
    pdf.save(`${canvasId}.pdf`);
  }

  goToMain() {
    this.router.navigate(['/main']);
  }
}
