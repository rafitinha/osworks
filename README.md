# osworks

#### Ignorar Alterações Apenas Localmente no Git

1. Alterar o arquivo exclude [$GIT_DIR/info/exclude]

```bash
2. git update-index --assume-unchanged <file-list> |
   git update-index --skip-worktree <file-list> |
   git update-index --no-skip-worktree <file-list> 
```

#### Atualizar Arquivo .gitignore
git rm -r --cached <file-name.extension>

 
 
