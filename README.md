# 本项目抄自jiesoul的 soul-talk 在其基础上做了部分修正  也不知道好还是不好

有对象的时候觉得很烦, 但是没有对象的时候又想念对象. 恩.


# soul-talk-web


首先确保你安装了 java clojure nodejs yarn 

安装 NPM 依赖：
```bash
yarn install
```

打包外部 JS
```bash
yarn webpack
```

开发运行
```bash
clojure -A:fig:dev
```

打包生产
```bash
clojure -A:fig:prod
```

打包生产并运行服务
```bash
clojure -A:fig:prod -s

## http://localhost:9500
```

开源许可 MIT
