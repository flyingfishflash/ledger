// modules (angular)
import { APP_INITIALIZER } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { FlexLayoutModule } from "@angular/flex-layout";
import { HttpClientModule } from "@angular/common/http";
import { NgModule } from "@angular/core";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";

// modules
import { AppRoutingModule } from "app/app-routing.module";
import { CoreModule } from "@core/core.module";
import { SharedModule } from "@shared/shared.module";
import { LoginModule } from "@modules/login/login.module";

// components
import { AppComponent } from "app/app.component";
import { AuthLayoutComponent } from "./layout/auth-layout/auth-layout.component";
import { ContentLayoutComponent } from "./layout/content-layout/content-layout.component";
import { HeadingComponent } from "./layout/heading/heading.component";

import { AppConfig } from "app/app-config";
import { initConfig } from "app/app-config";

import {
  InjectableRxStompConfig,
  RxStompService,
  rxStompServiceFactory,
} from "@stomp/ng2-stompjs";

// import { rxStompConfig } from './shared/rx-stomp.config';

@NgModule({
  bootstrap: [AppComponent],

  declarations: [
    AppComponent,
    AuthLayoutComponent,
    ContentLayoutComponent,
    HeadingComponent,
  ],

  exports: [FlexLayoutModule],

  imports: [
    AppRoutingModule,
    BrowserModule,
    FlexLayoutModule,
    HttpClientModule,
    CoreModule,
    LoginModule,
    NoopAnimationsModule,
    SharedModule,
  ],

  providers: [
    AppConfig,
    {
      provide: APP_INITIALIZER,
      useFactory: initConfig,
      deps: [AppConfig],
      multi: true,
    },
    RxStompService,
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
})
export class AppModule {}
