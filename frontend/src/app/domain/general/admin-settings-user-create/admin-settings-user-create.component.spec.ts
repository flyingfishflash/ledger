import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing'

import { AdminSettingsUserCreateComponent } from './admin-settings-user-create.component'

describe('AdminSettingsUserCreateComponent', () => {
  let component: AdminSettingsUserCreateComponent
  let fixture: ComponentFixture<AdminSettingsUserCreateComponent>

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [AdminSettingsUserCreateComponent],
    }).compileComponents()
  }))

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminSettingsUserCreateComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
