import { AppConfig, initConfig } from './app-config';
import { BrowserModule } from '@angular/platform-browser';
// import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { Inject, APP_INITIALIZER } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

import { FlexLayoutModule } from '@angular/flex-layout';
import { InjectableRxStompConfig, RxStompService, rxStompServiceFactory } from '@stomp/ng2-stompjs';

//import { AppConfigService } from './_services/app-config.service';
//import { AppConfig } from './app-config'
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AccountsTableComponent } from './accounts-table/accounts-table.component';
import { AccountsTreeComponent } from './accounts-tree/accounts-tree.component';
import { HeadingComponent } from './heading/heading.component';
import { ImportComponent } from './import/import.component';
import { LedgerMaterialModule } from './shared/material-module';
// import { rxStompConfig } from './shared/rx-stomp.config';

import { PadWithSpacesPipe } from './shared/pad-with-spaces.pipe';

import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { ProfileComponent } from './profile/profile.component';

import { authInterceptorProviders } from './_interceptors/auth.interceptor';
import { AdminSettingsComponent } from './admin-settings/admin-settings.component';
import { AdminSettingsUserCreateComponent } from './admin-settings-user-create/admin-settings-user-create.component';

@NgModule({
  imports: [
    AppRoutingModule,
    BrowserModule,
    FlexLayoutModule,
    FormsModule,
    HttpClientModule,
    NoopAnimationsModule,
    ReactiveFormsModule,
    LedgerMaterialModule
  ],

  declarations: [
    AppComponent,
    AccountsTableComponent,
    AccountsTreeComponent,
    HeadingComponent,
    ImportComponent,
    PadWithSpacesPipe,
    LoginComponent,
    HomeComponent,
    ProfileComponent,
    AdminSettingsComponent,
    AdminSettingsUserCreateComponent,
  ],

  providers: [
    authInterceptorProviders,
    AppConfig,
    { provide: APP_INITIALIZER,
      useFactory: initConfig,
      deps: [AppConfig],
      multi: true},
    RxStompService
/*     {
      provide: InjectableRxStompConfig,
      useValue: rxStompConfig
    },
    {
      provide: RxStompService,
      useFactory: rxStompServiceFactory,
      deps: [InjectableRxStompConfig]
    }  */
  ],

  bootstrap: [AppComponent]
})

export class AppModule { }
