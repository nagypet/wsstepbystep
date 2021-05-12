/*
 * Copyright header
 * The ultimate spring based webservice template project.
 * Author Peter Nagy <nagy.peter.home@gmail.com>
 */

import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LayoutComponent} from './layout/layout.component';
import {HeaderComponent} from './header/header.component';
import {FooterComponent} from './footer/footer.component';
import {SettingsComponent} from './settings/settings.component';
import {CertificatesComponent} from './certificates/certificates.component';
import {RouterModule, Routes} from '@angular/router';
import {AdminService} from './admin.service';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {AuthInterceptor} from './AuthInterceptor';
import {ToastrModule} from 'ngx-toastr';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {LoginComponent} from './login/login.component';
import {BrowserModule} from '@angular/platform-browser';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {KeystoreComponent, NgbdModalContent} from './certificates/keystore/keystore.component';
import {AboutComponent} from './about/about.component';
import {TabSetComponent} from './tab-set/tab-set.component';
import {AuthGuard} from './auth.guard';
import {AppAuthGuard} from './app.authguard';
import {AuthService} from './auth.service';
import {KeycloakService} from 'keycloak-angular';


export const routes: Routes = [
  {path: '', redirectTo: '/tab-settings', pathMatch: 'full'},
  {
    path: '', component: TabSetComponent,
    children: [
      {path: 'tab-settings', component: SettingsComponent},
      {path: 'tab-keystore', component: CertificatesComponent, canActivate: [AppAuthGuard]},
      {path: 'tab-truststore', component: CertificatesComponent, canActivate: [AuthGuard]},
    ],
  },
  {path: 'login', component: LoginComponent},
  {path: 'about', component: AboutComponent},
];

@NgModule({
  declarations: [
    LayoutComponent,
    HeaderComponent,
    FooterComponent,
    SettingsComponent,
    CertificatesComponent,
    LoginComponent,
    KeystoreComponent,
    NgbdModalContent,
    AboutComponent,
    TabSetComponent],
  imports: [
    CommonModule,
    BrowserModule,
    NgbModule,
    RouterModule.forRoot(routes, {relativeLinkResolution: 'legacy'}),
    HttpClientModule,
    ToastrModule.forRoot(),
    BrowserAnimationsModule,
  ],
  providers: [
    AdminService,
    AuthService,
    KeycloakService,
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true}
  ],
  exports: [LayoutComponent],
  entryComponents: [NgbdModalContent]
})
export class UiModule {
}
