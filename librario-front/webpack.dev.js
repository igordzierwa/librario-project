const BrowserSyncPlugin = require('browser-sync-webpack-plugin');
const ExtractTextPlugin = require('extract-text-webpack-plugin');
const merge = require('webpack-merge');
const WebpackNotifierPlugin = require('webpack-notifier');
const WriteFilePlugin = require('write-file-webpack-plugin');
const path = require('path');
const prodURL = require('./global_conf.js');
const common = require('./webpack.common');

const VueLoaderPlugin = require('vue-loader/lib/plugin')


const webpack = require('webpack')
const HtmlWebpackPlugin = require('vue-html-webpack-plugin')



module.exports = merge(common, {
  mode: 'development',
  devtool: 'inline-source-map',

    devServer: {
      port: 9000,
      watchContentBase: true,
        inline: true,
      historyApiFallback: true,
      hot: true,
      https: true,
      //stats: { chunks:false },
      headers: {  'Access-Control-Allow-Origin': '*',
          'Access-Control-Allow-Headers': 'Origin, X-Requested-With, Content-Type, Accept' },
  },
  module: {
    rules: [
        {
            test: /\.vue$/,
            loader: 'vue-loader',
            options: {
                loaders: {
                    // Since sass-loader (weirdly) has SCSS as its default parse mode, we map
                    // the "scss" and "sass" values for the lang attribute to the right configs here.
                    // other preprocessors should work out of the box, no loader config like this necessary.
                    'scss': [
                        'vue-style-loader',
                        'css-loader',
                        'sass-loader'
                    ],
                    'sass': [
                        'vue-style-loader',
                        'css-loader',
                        'sass-loader?indentedSyntax'
                    ]
                }
                // other vue-loader options go here
            }
        },
        {
            test: /\.scss$/,
            use: [
                'vue-style-loader',
                'css-loader',
                'sass-loader'
            ],
        },
        {
            test: /\.sass$/,
            use: [
                'vue-style-loader',
                'css-loader',
                'sass-loader?indentedSyntax'
            ],
        },
        {
            test: /\.css$/,
            use: [
                'vue-style-loader',
                'css-loader'
            ],
        },
        {
            test: /\.(eot|svg|ttf|woff|woff2)$/,
            loader: 'url-loader'
        },
    ],
  },
    output: {
        filename: '[name].js',
        path: path.resolve(__dirname, 'dist'),
        publicPath: 'https://localhost:9000/dist/',
    },
  plugins: [
    // https://www.npmjs.com/package/webpack-notifier
    // https://github.com/gajus/write-file-webpack-plugin
    // https://github.com/Va1/browser-sync-webpack-plugin
      new VueLoaderPlugin({
      }),
      new HtmlWebpackPlugin({
          filename: "index.html", // Write the file to <public-path>/graphql/index.html
          template: "index.html", // path to template
          vue: true,
          inject: true
      }),

    // new BrowserSyncPlugin(
    //   // BrowserSync options
    //     {
    //
    //         host: 'localhost',
    //         proxy: prodURL.url(),
    //         port: 3000,
    //         files: ["dist/*.css"],
    //         ignore: ["**/*.js", "**/*.json"],
    //
    //         rewriteRules: [
    //             {
    //                 match: /\/app_basket_dev\/\w+\/style.css/g,
    //                 replace: 'https://localhost:9000/dist/style.css'
    //             },
    //             {
    //                 match: /src="\w+.+\/app_basket_dev\/\w+\/script.js\?v=\d{1}.\d{1}"/g,
    //                 replace: 'src="https://localhost:9000/dist/script.js"'
    //             },
    //             {
    //                 match: /\/app_basket\/\w+\/style.css/g,
    //                 replace: 'https://localhost:9000/dist/style.css'
    //             },
    //             {
    //                 match: /src="\w+.+\/app_basket\/\w+\/script.js\?v=\d{1}.\d{1}"/g,
    //                 replace: 'src="https://localhost:9000/dist/script.js"'
    //             }
    //         ]
    //     },
    //     // plugin options
    //     {
    //         // prevent BrowserSync from reloading the page
    //         // and let Webpack Dev Server take care of this
    //
    //         reload: false,
    //         injectCss: true,
    //         injectChanges: true
    //     }
    // ),
  ],
});
