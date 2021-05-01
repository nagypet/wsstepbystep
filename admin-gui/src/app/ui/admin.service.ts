/*
 * Copyright header
 * The ultimate spring based webservice template project.
 * Author Peter Nagy <nagy.peter.home@gmail.com>
 */

import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpUrlEncodingCodec, HttpBackend} from '@angular/common/http';
import {Observable} from 'rxjs';
import {GlobalService} from './global.service';
import {CertificateFile} from '../modell/keystore';


@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private httpSilent: HttpClient;

  constructor(
    private http: HttpClient,
    private globalService: GlobalService,
    private handler: HttpBackend
  ) {
    this.httpSilent = new HttpClient(handler);
  }

  private getServiceUrl(path: string): string {
    const host = window.location.hostname;
    const protocol = window.location.protocol;
    const port = window.location.port;

    let url = '';
    if (port === '4200') {
      // running on dev environment
      url = 'http://localhost:4200' + path;
    } else {
      url = protocol + '//' + host + ':' + port + path;
    }

    console.log('Connecting to \'' + url + '\'');
    return url;
  }


  private removeWhitespacesFromString(input: string): string {
    const codec = new HttpUrlEncodingCodec();
    return codec.encodeValue(input);
    // input.replace(' ', '%20');
  }


  public getVersionInfo(): Observable<any> {

    return this.http.get(this.getServiceUrl('/admin/version'));
  }

  public getSettings(): Observable<any> {
    return this.http.get(this.getServiceUrl('/admin/settings'));
  }

  public getSettingsSilently(): Observable<any> {
    return this.httpSilent.get(this.getServiceUrl('/admin/settings'));
  }

  public postShutdown(): Observable<any> {
    return this.http.post(this.getServiceUrl('/admin/shutdown'), '');
  }

  public getAuthenticationToken(userName?: string, password?: string): Observable<any> {
    if (userName === undefined || password === undefined) {
      return this.httpSilent.get(this.getServiceUrl('/authenticate'));
    } else {
      return this.http.get(this.getServiceUrl('/authenticate'), this.getAuthHeader(userName, password));
    }
  }

  public logout(): Observable<any> {
    return this.http.get(this.getServiceUrl('/logout'));
  }

  public getKeystore(): Observable<any> {
    return this.http.get(this.getServiceUrl('/keystore'));
  }


  public saveKeystore(): Observable<any> {
    return this.http.post(this.getServiceUrl('/keystore'), null);
  }


  public getEntriesFromCert(certFile: CertificateFile): Observable<any> {
    return this.http.post(this.getServiceUrl('/keystore/certificates'), certFile);
  }

  public importCertificateIntoKeystore(certFile: CertificateFile, alias: string): Observable<any> {
    return this.http.post(this.getServiceUrl('/keystore/privatekey'), {certificateFile: certFile, alias: alias});
  }

  public removeCertificateFromKeystore(alias: string): Observable<any> {
    // we have to remove white spaces from the alias name
    return this.http.delete(this.getServiceUrl('/keystore/privatekey/' + this.removeWhitespacesFromString(alias)));
  }

  public getTruststore(): Observable<any> {
    return this.http.get(this.getServiceUrl('/truststore'));
  }

  public importCertificateIntoTruststore(certFile: CertificateFile, alias: string): Observable<any> {
    return this.http.post(this.getServiceUrl('/truststore/certificate'), {certificateFile: certFile, alias: alias});
  }

  public removeCertificateFromTruststore(alias: string): Observable<any> {
    // we have to remove white spaces from the alias name
    return this.http.delete(this.getServiceUrl('/truststore/certificate/' + this.removeWhitespacesFromString(alias)));
  }


  private getAuthHeader(userName?, password?): { headers: HttpHeaders } {
    let authorizationHeader = '';
    if (userName !== undefined && password !== undefined) {
      const credentials = userName + ':' + password;
      authorizationHeader = 'Basic ' + btoa(credentials);
    } else {
      const credentials = this.globalService.getToken();
      authorizationHeader = 'Bearer ' + credentials;
    }

    // console.log(credentials);

    return {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': authorizationHeader
      })
    };
  }
}
