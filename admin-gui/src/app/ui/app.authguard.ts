import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {KeycloakAuthGuard, KeycloakService} from 'keycloak-angular';
import {AuthService} from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AppAuthGuard extends KeycloakAuthGuard {

  constructor(
    protected readonly router: Router,
    protected readonly keycloakService: KeycloakService,
    protected authService: AuthService
  ) {
    super(router, keycloakService);
  }

  async isAccessAllowed(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Promise<boolean | UrlTree> {

    const redirectURL = window.location.origin + '/#' + state.url;
    console.log('---------------- AppAuthGuard BEGIN -----------------------');
    console.log('Redirect URI: ' + redirectURL);

    if (!this.authenticated) {
      await this.keycloakService.login({
        redirectUri: redirectURL,
      });
    }

    if (!this.authService.isAuthenticated()) {
      const token = await this.keycloakService.getToken();

      console.log('Keycloak token: ' + token);
      await this.authService.authenticateWithKeycloakToken(token).toPromise();

    }
    console.log('---------------- AppAuthGuard END: ' + this.authService.isAuthenticated());
    return this.authService.isAuthenticated();
  }
}

