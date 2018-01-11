<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Index</title>
  <link rel="stylesheet" href="bulma.css">
</head>
<body>
  <nav class="navbar" role="navigation" aria-label="main navigation">
    <div class="navbar-brand">
      <a class="navbar-item" href="/">
        Servidor Java
      </a>
      <a class="navbar-item" href="/">
        Home
      </a>
      <a class="navbar-item" href="index2.html">
        Outra página
      </a>
      <a class="navbar-item" href="pagina.php">
        Página PHP
      </a>
      <button class="button navbar-burger">
        <span></span>
        <span></span>
        <span></span>
      </button>
    </div>
  </nav>
  <section class="hero is-info is-fullheight">
    <div class="hero-body">
      <div class="container has-text-centered">
        <h1 class="title">
          Essa página é feita em php
        </h1>
        <h2 class="subtitle">
          Para comprovar, estou calculando a soma de 30 + 20 = <?php $soma = 30+20; echo $soma; ?>
        </h2>
      </div>
    </div>
  </section>
</body>
</html>
