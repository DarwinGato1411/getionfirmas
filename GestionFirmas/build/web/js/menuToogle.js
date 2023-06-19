const menuToogle = () => {
    const menu = document.querySelector('.nav');
    const btnMenu = document.querySelector('.btnMenu');
    const hideMenu = document.querySelector('.contenedorMC');
    const cerrarMenu = document.querySelector('.panelCerrarMenu');

    btnMenu.addEventListener('click', () => {
        menu.classList.add('desplegar');
        cerrarMenu.style.display = 'block';
    });
    cerrarMenu.addEventListener('click', () => {
        cerrarMenu.style.display = 'none';
        menu.classList.remove('desplegar');
    });
};

const screen = () => {
    const menu = document.querySelector('.nav');
    const cerrarMenu = document.querySelector('.panelCerrarMenu');
    window.addEventListener('resize', function () {
        let ancho = window.innerWidth;
        
        if (ancho >= 1024) {
            cerrarMenu.style.display = 'none';
            menu.classList.remove('desplegar');
        }
    });
};


(() => {
    zk.afterMount(function () {
        menuToogle();
        screen();
    });
})();
