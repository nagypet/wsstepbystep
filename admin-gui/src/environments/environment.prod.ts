/*
 * Copyright header
 * The ultimate spring based webservice template project.
 * Author Peter Nagy <nagy.peter.home@gmail.com>
 */

import {KeycloakOptions} from 'keycloak-angular';

const keycloakConfig: KeycloakOptions = {
  config: {
    url: 'https://keycloak.wsstepbystep.perit.hu:8543/auth',
    realm: 'public-library',
    clientId: 'admin-gui'
  },
  initOptions: {
    onLoad: 'check-sso',
    silentCheckSsoRedirectUri:
      window.location.origin + '/assets/silent-check-sso.html',
  },
};


export const environment = {
  keycloakOptions: keycloakConfig,
  production: true
};
