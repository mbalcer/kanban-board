import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Project} from "../../home/projects/project";
import {RegisterService} from "../../flow-register/register.service";
import {FlowRegister} from "../../flow-register/flow-register";
import {NotificationService} from "../../notification.service";

@Component({
  selector: 'app-dialog-flow-chart',
  templateUrl: './dialog-flow-chart.html',
  styleUrls: ['./dialog-flow-chart.css']
})
// tslint:disable-next-line:component-class-suffix
export class DialogFlowChart {

  results = [];
  view: any[] = [1050, 500];
  colorScheme = {
    domain: ['#ccbef4', '#8c66ff', '#3360cc', '#200080']
  };

  constructor(public dialogRef: MatDialogRef<DialogFlowChart>,
              private registerService: RegisterService,
              private notification: NotificationService,
              @Inject(MAT_DIALOG_DATA) public data: Project) {
    this.getFlowRegister();
  }

  getFlowRegister(): void {
    this.results = [];
    this.registerService.getAllByProject(this.data).subscribe(registerTab => {
      this.mapResults(registerTab.sort((r1, r2) => r1.date.localeCompare(r2.date)));
    });
  }

  mapResults(registerTab: FlowRegister[]): void {
    let resultMap = new Map<string, SeriesEntry[]>();
    resultMap.set("TODO", []);
    resultMap.set("IN_PROGRESS", []);
    resultMap.set("TESTING", []);
    resultMap.set("DONE", []);
    registerTab.forEach(register => {
      Object.keys(register.flow).map(key => {
        let currentSeries = [];
        if (resultMap.has(key)) {
          currentSeries = resultMap.get(key);
        }
        currentSeries.push(new SeriesEntry(register.date, register.flow[key]));
        resultMap.set(key, currentSeries);
      })
    });
    this.convertToJson(resultMap);
  }

  convertToJson(resultMap: Map<string, SeriesEntry[]>): void {
    resultMap.forEach((value, key) => {
      let seriesValue = [];
      value.forEach(entry => {
        seriesValue.push({
          name: entry.date,
          value: entry.count
        })
      });
      this.results = [...this.results, {
        name: this.translateState(key),
        series: seriesValue
      }];
    });
  }

  translateState(state: string): string {
    if (state == "TODO") {
      return "Do zrobienia";
    } else if (state == "IN_PROGRESS") {
      return "W trakcie";
    } else if (state == "TESTING") {
      return "Testowanie";
    } else if (state == "DONE") {
      return "Zrobione";
    } else {
      return "Inne";
    }
  }

  refresh(): void {
    this.registerService.updateByProject(this.data).subscribe(() => {
      this.getFlowRegister();
      this.notification.success('Rejestr został zaktualizowany');
    }, error => this.notification.error(error.error.message));
  }

  closeDialog(): void {
    this.dialogRef.close();
  }

}

export class SeriesEntry {
  date: string;
  count: number;

  constructor(date: string, count: number) {
    this.date = date;
    this.count = count;
  }
}
