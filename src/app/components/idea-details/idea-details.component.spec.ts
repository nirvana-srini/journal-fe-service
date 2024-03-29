import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IdeasDetailsComponent } from './idea-details.component';

describe('IdeasDetailsComponent', () => {
  let component: IdeasDetailsComponent;
  let fixture: ComponentFixture<IdeasDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [IdeasDetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IdeasDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
