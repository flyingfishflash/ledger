import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing'

import { AccountsTableComponent } from './accounts-table.component'

describe('AccountsTableComponent', () => {
  let component: AccountsTableComponent
  let fixture: ComponentFixture<AccountsTableComponent>

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [AccountsTableComponent],
    }).compileComponents()
  }))

  beforeEach(() => {
    fixture = TestBed.createComponent(AccountsTableComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })
})
