import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChildUserManagementComponent } from './child-user-management.component';

describe('ChildUserManagementComponent', () => {
  let component: ChildUserManagementComponent;
  let fixture: ComponentFixture<ChildUserManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChildUserManagementComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ChildUserManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
