import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // 1. Get the token from local storage (or your Auth Service)
  const token = localStorage.getItem('token');

  // 2. If there is a token, clone the request and add the Authorization header
  if (token) {
    const clonedRequest = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });

    // 3. Pass the cloned request to the next handler
    return next(clonedRequest);
  }

  // 4. If there is no token (e.g., before login), just pass the original request
  return next(req);
};
