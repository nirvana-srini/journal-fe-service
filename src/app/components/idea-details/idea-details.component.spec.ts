import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IdeaDetailsComponent } from './idea-details.component';

describe('IdeaDetailsComponent', () => {
  let component: IdeaDetailsComponent;
  let fixture: ComponentFixture<IdeaDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [IdeaDetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IdeaDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
