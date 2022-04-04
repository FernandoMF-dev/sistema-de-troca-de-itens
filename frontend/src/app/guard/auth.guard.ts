import { Route } from '@angular/compiler/src/core';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, CanLoad, Router, RouterStateSnapshot, UrlSegment, UrlTree } from '@angular/router';

import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

import { LoginService } from './../services/login.service';

@Injectable({
    providedIn: 'root'
})
export class AuthGuard implements CanActivate, CanLoad {

    constructor(
        private router: Router,
        private loginService: LoginService
    ) {
    }

    canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
        return this.validarLogin();
    }

    canLoad(route: Route, segments: UrlSegment[]): Observable<boolean> | Promise<boolean> | boolean {
        return this.validarLogin();
    }

    validarLogin(): Observable<boolean> | boolean {
        let token = localStorage.getItem('token');

        if (token) {
            return this.loginService.buscarPorToken(token).pipe(
                map(() => {
                    return true;
                }),
                catchError(() => {
                    localStorage.removeItem('token');
                    localStorage.removeItem('usuario');
                    this.router.navigate(['./login']);
                    return of(false);
                })
            );
        } else {
            this.router.navigate(['./login']);
            return false;
        }
    }

}
