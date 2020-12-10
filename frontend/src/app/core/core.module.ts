// angular
import { HttpClientModule } from "@angular/common/http";
import { HTTP_INTERCEPTORS } from "@angular/common/http";
import { NgModule } from "@angular/core";
import { Optional } from "@angular/core";
import { SkipSelf } from "@angular/core";

// core
import { AuthGuard } from "./guards/auth.guard";
import { AuthInterceptor } from "./authentication/basic-auth.interceptor";
import { ErrorHandlerModule } from "./errors/error-handler.module";
import { StorageService } from "./storage/storage.service";
import { throwIfAlreadyLoaded } from "./guards/module-import.guard";
import { ValidationService } from "./validation/validation.service";

@NgModule({
  declarations: [],
  imports: [ErrorHandlerModule, HttpClientModule],
  providers: [
    AuthGuard,
    //NoAuthGuard,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,
    },
    StorageService,
    ValidationService,
  ],
})
export class CoreModule {
  constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
    throwIfAlreadyLoaded(parentModule, "CoreModule");
  }
}
