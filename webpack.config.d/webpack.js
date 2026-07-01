// Dev server: enable hot reload and SPA history fallback so deep links (e.g. /#contact routing,
// or any client route) serve index.html instead of 404ing. Production build drops source maps.
if (config.devServer) {
    // HMR corrupts the Kotlin/JS module graph (kotlinx-html "reading 'a'" at init). Use plain
    // live-reload (full page refresh on change) instead - stable for this stack.
    config.devServer.hot = false;
    config.devServer.liveReload = true;
    config.devServer.historyApiFallback = true;
    config.devServer.client = {
        overlay: true
    };
} else {
    config.devtool = undefined;
}
