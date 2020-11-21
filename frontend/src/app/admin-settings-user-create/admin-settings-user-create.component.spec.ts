import { async, ComponentFixture, TestBed } from "@angular/core/testing";

import { AdminSettingsUserCreateComponent } from "./admin-settings-user-create.component";

describe("AdminSettingsUserCreateComponent", () => {
  let component: AdminSettingsUserCreateComponent;
  let fixture: ComponentFixture<AdminSettingsUserCreateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AdminSettingsUserCreateComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminSettingsUserCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
