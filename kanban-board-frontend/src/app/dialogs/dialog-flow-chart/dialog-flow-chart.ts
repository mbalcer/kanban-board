import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Project} from "../../home/projects/project";

@Component({
  selector: 'app-dialog-flow-chart',
  templateUrl: './dialog-flow-chart.html',
  styleUrls: ['./dialog-flow-chart.css']
})
// tslint:disable-next-line:component-class-suffix
export class DialogFlowChart {

  results = [
    {
      "name": "Do zrobienia",
      "series": [
        {
          "name": "2010",
          "value": 15
        },
        {
          "name": "2011",
          "value": 14
        },
        {
          "name": "2012",
          "value": 13
        }
      ]
    },
    {
      "name": "W trakcie",
      "series": [
        {
          "name": "2010",
          "value": 10
        },
        {
          "name": "2011",
          "value": 10
        },
        {
          "name": "2012",
          "value": 9
        }
      ]
    },
    {
      "name": "Zrobione",
      "series": [
        {
          "name": "2010",
          "value": 1
        },
        {
          "name": "2012",
          "value": 2
        }
      ]
    }
  ];

  view: any[] = [1050, 500];
  colorScheme = {
    domain: ['#5AA454', '#A10A28', '#C7B42C']
  };

  constructor(
    public dialogRef: MatDialogRef<DialogFlowChart>,
    @Inject(MAT_DIALOG_DATA) public data: Project) {
  }

  refresh(): void {
  }

  closeDialog(): void {
    this.dialogRef.close();
  }

}
