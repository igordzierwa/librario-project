//const ExtractTextPlugin = require('extract-text-webpack-plugin');
const merge = require('webpack-merge');
const UglifyJSPlugin = require('uglifyjs-webpack-plugin');

const path = require('path');
const prodURL = require('./global_conf.js');
const common = require('./webpack.common');
const VueLoaderPlugin = require('vue-loader/lib/plugin')
const webpack = require('webpack')
const HtmlWebpackPlugin = require('vue-html-webpack-plugin');

const TerserPlugin = require('terser-webpack-plugin');
const BabelEnginePlugin = require('babel-engine-plugin');


module.exports = merge(common, {
    mode: 'production',
    module: {
        rules: [
            {
                test: /\.(vue)$/,
                loader: 'vue-loader',
                options: {
                    loaders: {
                        // Since sass-loader (weirdly) has SCSS as its default parse mode, we map
                        // the "scss" and "sass" values for the lang attribute to the right configs here.
                        // other preprocessors should work out of the box, no loader config like this necessary.
                        js: 'babel-loader', // just list loaders here
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
    // transpileDependencies: [
    //     'element-ui',
    // ],
    // optimization: {
    //     minimizer: [new TerserPlugin()],
    // },
    optimization: {
        // //minimize: false
        // minimizer: [new UglifyJSPlugin({
        //         sourceMap: false,
        //         uglifyOptions: {
        //             mangle: true,
        //             safari10: true,
        //             ecma: 5
        //         }
        //     }
        // )],
        // minimize: true,
        // minimizer: [
        //     new TerserPlugin({
        //         terserOptions: {
        //             ecma: 5,
        //             warnings: false,
        //             parse: {},
        //             compress: {},
        //             mangle: true, // Note `mangle.properties` is `false` by default.
        //             module: true,
        //             output: null,
        //             toplevel: false,
        //             nameCache: null,
        //             ie8: true,
        //             keep_classnames: undefined,
        //             keep_fnames: false,
        //             safari10: true,
        //         },
        //     }),
        // ],
    },
    output: {
        filename: '[name].js',
        path: path.resolve(__dirname, 'dist'),
        publicPath: "https://appstore.soolution.pl/app_basket/dist/",
    },
    plugins: [
        new VueLoaderPlugin(),
        new HtmlWebpackPlugin({
            filename: "production/index.html", // Write the file to <public-path>/graphql/index.html
            template: "index.html", // path to template
            vue: true,
            inject: true
        }),
        // new BabelEnginePlugin({
        //     presets: ['@vue/app']
        // })

        // new UglifyJSPlugin({
        //     uglifyOptions: {
        //         mangle: false,
        //         sourceMap: false,
        //         safari10: true,
        //         ecma: 5
        //     }
        // }),
        // new JavaScriptObfuscator ({
        //     rotateUnicodeArray: true
        // }, ['script.js'])
    ],
});
