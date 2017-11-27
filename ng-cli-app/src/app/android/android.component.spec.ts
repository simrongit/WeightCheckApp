import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AndroidComponent } from './android.component';

describe('AndroidComponent', () => {
  let component: AndroidComponent;
  let fixture: ComponentFixture<AndroidComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AndroidComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AndroidComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});