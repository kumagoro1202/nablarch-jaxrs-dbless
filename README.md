# nablarch-jaxrs-dbless

Nablarch 6u3 の `nablarch-container-jaxrs` アーキタイプから生成したプロジェクトを、DBを使わない構成に変更した検証用プロジェクトです。

## アーキタイプからの変更点

### rest-component-configuration.xml

- 削除：`nablarch/schema-config.xml`、`nablarch/common/code.xml`、`nablarch/core/db-base.xml`、`data-source.xml` の import
- 削除：ハンドラキューの `dbConnectionManagementHandler`、`transactionManagementHandler`
- 削除：`initializer` の `codeLoader`、`codeCache`、および `disposer`
- 追加：`nablarch/core/date/system-date.xml` の import

`systemTimeProvider` は元の構成では `data-source.xml` から間接的に import される `system-date.xml` で定義されています。
テスティングフレームワークがこのコンポーネントを参照するため、明示的に import しています。

### その他

- `env.properties` / `common.properties`：DB・コード管理・業務日付関連のプロパティを削除
- `pom.xml`：`nablarch-common-jdbc`、`nablarch-common-dao`、`nablarch-common-code-jdbc`、`nablarch-common-idgenerator-jdbc`、H2、HikariCP、gsp-dba-maven-plugin 関連の設定を削除
- テスト：`SimpleRestTestSupport` と `nablarch/test/test-data-dbless.xml` を使用
- DBアクセスを行うサンプル・EDM・DDL を削除し、検証用の `RelayAction` を追加

## 検証用アクション

| パス | 内容 |
| --- | --- |
| `GET /relay/ping` | `{"status":"ok"}` を返す |
| `POST /relay/echo` | 受け取ったJSONを返す（`@Domain` によるバリデーションあり） |
| `POST /relay/send` | TCPサーバへ1行送信し、応答を返す（接続先はシステムプロパティ `relay.tcp.host` / `relay.tcp.port`、既定値 `localhost:9999`） |

## 実行方法

```bash
mvn test
mvn jetty:run
curl http://localhost:9080/relay/ping
```
