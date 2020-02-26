import React from 'react';
import ReactDOM from 'react-dom';
window.React = React;
window.ReactDOM = ReactDOM;

import showdown from 'showdown';
window.showdown = showdown;

import hljs from 'highlight.js';
window.hljs = hljs;

import SimpleMDE from 'simplemde';
window.SimpleMDE = SimpleMDE;

import moment from 'moment';
import 'moment/locale/zh-cn'
window.moment = moment;

import reactMap from 'react-amap';
window.reactMap = reactMap;


// import anychart from 'anychart';
// window.anychart = anychart;

// import chartjs from 'chartjs';
// window.chartjs =  chartjs;

import * as antd from 'antd';
// import zhCN from 'antd/lib/locale-provider/zh_CN'
window.antd = antd;

import './default.less';

