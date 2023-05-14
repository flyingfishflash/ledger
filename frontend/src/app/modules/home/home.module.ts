// angular
import { NgModule } from '@angular/core';

// core and shared
import { SharedModule } from '@shared/shared.module';

// components
import { HomeComponent } from './home.component';
import { HomeRoutingModule } from './home.routing';

@NgModule({
  declarations: [HomeComponent],
  imports: [SharedModule, HomeRoutingModule],
  exports: [],
  providers: [],
})
export class HomeModule {}
