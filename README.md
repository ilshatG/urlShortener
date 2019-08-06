# urlShortener
Due to assignment this application provides an HTTP service that serves to shorten URLs, with the following functionalities:

- Registration Web address (API);
- Redirect client in accordance with the shortened URL;
- Usage Statistics (API).

The service has two parts: configuration and user.

1.1. Configuration part

The configuration part is invoked using REST calls with JSON payload and is used for:

a) Opening of accounts;
b) Registration of URLs in the 'Shortener' service;
c) Displaying stats.

1.2. Redirecting

Redirectes the client on the configured address with the configured http status.
