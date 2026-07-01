// Dev server: enable hot reload and SPA history fallback so deep links (e.g. /#contact routing,
// or any client route) serve index.html instead of 404ing. Production build drops source maps.
if (config.devServer) {
    config.devServer.hot = true;
    config.devServer.historyApiFallback = true;
    config.devServer.client = {
        overlay: false
    };
} else {
    config.devtool = undefined;
}
