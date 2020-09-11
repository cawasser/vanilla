
### Auto-Documentation
[Marginalia](https://github.com/gdeer81/marginalia) and more specifically [lein-marginalia](https://github.com/gdeer81/lein-marginalia)has been added as a plugin to this project to keep a healthy level of documentation of our codebase and architecture. The plugin essentially scans through the project and creates an html "wiki" of how our project operates. It takes all our in line comments and can be added to easily to explain certain namespaces, functions, or design decisions.

To run the plugin and generate the aforementioned wiki simply use:

```
lein marg
```

This will generate the file:

```
./docs/uberdoc.html
```

Here is the document it generates:

[marginalia-uberdoc](uberdoc.html)