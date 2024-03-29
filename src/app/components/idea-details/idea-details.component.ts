import { Component, Input, OnInit } from '@angular/core';
import { Idea } from '../../models/idea.model';

@Component({
  selector: 'app-ideas-details',
  templateUrl: './idea-details.component.html',
  styleUrl: './idea-details.component.css'
})
export class IdeasDetailsComponent implements OnInit{
  @Input() viewMode:boolean = false;

  @Input() currentIdea?: Idea = {
    id: 0,
    name: '',
    description: '',
    completed: false
  };

  ngOnInit(): void {
    //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    //Add 'implements OnInit' to the class.
    console.log(this.currentIdea);
  }
}
