import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

    constructor() {
    }

    intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {

        let token = localStorage.getItem('token');

        if (token) {
            const req = request.clone({
                setHeaders: {
                    Token: `${ token }`
                }
            });

            return next.handle(req);
        }

        return next.handle(request);
    }
}
