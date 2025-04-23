import {
  Component,
  inject,
  ViewChild,
  OnInit,
  ElementRef,
} from '@angular/core';
import { UserService } from '../user.service';
import { CommonModule } from '@angular/common';
import { BehaviorSubject, Observable } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { CheckResponse } from '../../interfaces/check-response';
import * as JXG from 'jsxgraph';

@Component({
  selector: 'app-home',
  imports: [CommonModule, FormsModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent {
  private userService: UserService;
  private http: HttpClient;
  private router: Router;

  notifications: { message: string; fadeOut: boolean }[] = [];
  username$: Observable<string | undefined>;

  constructor() {
    this.userService = inject(UserService);
    this.username$ = new BehaviorSubject<string | undefined>(undefined);
    this.http = inject(HttpClient);
    this.router = inject(Router);
  }

  addNotification(message: string): void {
    this.notifications.push({ message, fadeOut: false });
    setTimeout(() => {
      this.notifications[this.notifications.length - 1].fadeOut = true;
    }, 5000);
    setTimeout(() => {
      this.notifications.shift();
    }, 6000);
  }

  ngOnInit(): void {
    this.userService.getUsernameObservable().subscribe((username) => {
      (this.username$ as BehaviorSubject<string | undefined>).next(username);

      if (username) {
        this.loadPoints(username);
      }
    });
    this.initializeBoard();
  }

  validateInput(event: KeyboardEvent): void {
    const allowedKeys = [
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '.',
      '-',
    ];

    if (
      !allowedKeys.includes(event.key) &&
      event.key !== 'Backspace' &&
      event.key !== 'Delete' &&
      event.key !== 'ArrowLeft' &&
      event.key !== 'ArrowRight'
    ) {
      event.preventDefault();
    }
  }

  validateValue(): void {
    const regex = /^-?\d*(\.\d*)?$/;

    if (!regex.test(this.yValue)) {
      const matches = this.yValue.match(/^-?\d*(\.\d*)?/);
      this.yValue = matches ? matches[0] : '';
    }
  }

  selectedX: number = 0;
  selectedR: number = 1;
  yValue: string = '';

  radioOptionsforX: number[] = [-3, -2, -1, 0, 1, 2, 3, 4, 5];
  radioOptionsforR: number[] = [1, 2, 3, 4, 5];

  onSubmit(): void {
    this.username$.subscribe((username) => {
      const data = {
        X: this.selectedX,
        Y: this.yValue,
        R: this.selectedR,
        OWNER: username,
      };

      this.http
        .post<CheckResponse>('http://127.0.0.1:25664/controller', data, {
          headers: {
            'Content-Type': 'application/json',
            Authorization: 'Bearer token',
            'X-Request-Type': 'Check',
          },
          withCredentials: true,
        })
        .subscribe({
          next: (response) => {
            console.log('Check successful', response);
            this.addPointToR(
              this.selectedR,
              this.selectedX,
              parseFloat(this.yValue),
              response.FLAG
            );
            if (response.FLAG) {
              this.addNotification('ПОПАЛ, ДА ТЫ СНАЙПЕР');
            } else {
              this.addNotification('ПРОМАЗАЛ((');
            }
          },
          error: (error) => {
            if (error.status === 400) {
              console.error('Bad request:', error.error);
              this.addNotification(
                'Ошибка: Проверьте данные, какой то мусор отправили'
              );
            } else if (error.status === 500) {
              console.error('Server error:', error.error);
              this.addNotification('Сервер лег, молодец блять...');
            } else {
              console.error('Unexpected error:', error);
              this.addNotification('Ошибка: Не известно какая');
            }
          },
          complete: () => {
            console.log('Check request completed');
          },
        });
    });
  }

  @ViewChild('jxgBox') jxgBox!: ElementRef;
  pointsByR: { [key: number]: any[] } = {};
  board: any;
  private graphObjects: any[] = [];

  ngAfterViewInit(): void {
    this.initializeBoard();

    if (this.board) {
      this.board.on('down', (event: any) => {
        const coords = this.board.getUsrCoordsOfMouse(event);
        console.log('Координаты на графике:', coords);
        this.sendCoords(coords[0], coords[1], this.selectedR);
      });
    }
  }

  initializeBoard(): void {
    if (this.jxgBox && this.jxgBox.nativeElement) {
      if (!this.board) {
        this.board = JXG.JSXGraph.initBoard(this.jxgBox.nativeElement, {
          boundingbox: [-6, 6, 6, -6],
          axis: true,
          defaultAxes: {
            x: {
              ticks: { color: 'red', drawZero: true, label: { color: 'red' } },
            },
            y: { ticks: { color: 'red', label: { color: 'red' } } },
          },
        });
      }
    }

    this.redrawGraphs();
  }

  clearOldGraphs(): void {
    this.graphObjects.forEach((graph) => {
      graph.remove();
    });
    this.graphObjects = [];
  }

  redrawGraphs(): void {
    if (!this.board) return;

    this.clearOldGraphs();

    this.createGraphsAndAreas(this.selectedR);
  }
  createGraphsAndAreas(r: number): void {
    const radio = r;

    const graph1 = this.board.create('functiongraph', [
      (x: number) => -x / 2 + radio / 2,
      0,
      radio,
    ]);
    this.graphObjects.push(graph1);

    const i1 = this.board.create('integral', [[0, radio], graph1], {
      label: { visible: false },
      curveLeft: { visible: false },
      curveRight: { visible: false },
      baseRight: { visible: false },
      baseLeft: { visible: false },
      fillColor: 'rgba(0, 255, 0, 0.3)',
    });
    this.graphObjects.push(i1);

    const graph2 = this.board.create('functiongraph', [
      (x: number) => radio / 2,
      -radio,
      0,
    ]);
    this.graphObjects.push(graph2);

    const i2 = this.board.create('integral', [[-radio, 0], graph2], {
      label: { visible: false },
      curveLeft: { visible: false },
      curveRight: { visible: false },
      baseRight: { visible: false },
      baseLeft: { visible: false },
      fillColor: 'rgba(0, 255, 0, 0.3)',
    });
    this.graphObjects.push(i2);

    const graph3 = this.board.create('functiongraph', [
      (x: number) => -Math.sqrt((radio * radio) / 4 - x * x),
      -radio / 2,
      0,
    ]);
    this.graphObjects.push(graph3);

    const i3 = this.board.create('integral', [[-radio / 2, 0], graph3], {
      label: { visible: false },
      curveLeft: { visible: false },
      curveRight: { visible: false },
      baseRight: { visible: false },
      baseLeft: { visible: false },
      fillColor: 'rgba(0, 255, 0, 0.3)',
    });
    this.graphObjects.push(i3);

    this.showPointsForCurrentR(r);
  }

  showPointsForCurrentR(r: number): void {
    Object.keys(this.pointsByR).forEach((key) => {
      const currentR = Number(key);
      this.pointsByR[currentR].forEach((point) => {
        if (currentR === r) {
          point.setAttribute({ visible: true });
        } else {
          point.setAttribute({ visible: false });
        }
      });
    });
  }

  onRValueChange(newR: number): void {
    this.selectedR = newR;
    this.redrawGraphs();
  }

  addPointToR(r: number, x: number, y: number, flag: boolean): void {
    let color = '';
    if (!this.pointsByR[r]) {
      this.pointsByR[r] = [];
    }

    if (flag) {
      color = 'Green';
    } else {
      color = 'Red';
    }

    const point = this.board.create('point', [x, y], {
      name: `(${x.toFixed(2)}, ${y.toFixed(2)})`,
      size: 4,
      color: color,
    });

    this.pointsByR[r].push(point);

    this.redrawGraphs();
  }

  loadPoints(username: string): void {
    const url = `http://127.0.0.1:25664/controller?username=${username}`;

    this.http
      .get<any>(url, {
        headers: {
          Authorization: 'Bearer token',
          'X-Request-Type': 'Check',
        },
        withCredentials: true,
      })
      .subscribe({
        next: (response) => {
          console.log('Loaded response:', response);

          if (Array.isArray(response)) {
            response.forEach((pointData) => {
              this.addPointToR(
                pointData.r,
                pointData.x,
                pointData.y,
                pointData.flag
              );
            });
          } else {
            console.error('Expected an array, but got:', response);
          }
        },
        error: (error) => {
          console.error('Error loading points:', error);
        },
        complete: () => {
          console.log('Points loading completed');
        },
      });
  }

  sendCoords(X: number, Y: number, R: number): void {
    this.username$.subscribe((username) => {
      const data = {
        X: X,
        Y: Y,
        R: R,
        OWNER: username,
      };

      this.http
        .post<CheckResponse>('http://127.0.0.1:25664/controller', data, {
          headers: {
            'Content-Type': 'application/json',
            Authorization: 'Bearer token',
            'X-Request-Type': 'Check',
          },
          withCredentials: true,
        })
        .subscribe({
          next: (response) => {
            console.log('Check successful', response);
            this.addPointToR(response.R, response.X, response.Y, response.FLAG);
            if (response.FLAG) {
              this.addNotification('ПОПАЛ, ДА ТЫ СНАЙПЕР');
            } else {
              this.addNotification('ПРОМАЗАЛ((');
            }
          },
          error: (error) => {
            if (error.status === 400) {
              console.error('Bad request:', error.error);
              this.addNotification(
                'Ошибка: Проверьте данные, какой то мусор отправили'
              );
            } else if (error.status === 500) {
              console.error('Server error:', error.error);
              this.addNotification('Сервер лег, молодец блять...');
            } else {
              console.error('Unexpected error:', error);
              this.addNotification('Ошибка: Не известно какая');
            }
          },
          complete: () => {
            console.log('Check request completed');
          },
        });
    });
  }
}
