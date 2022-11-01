<template>
    <div>
        <header>
            <el-row>
                <el-col :lg="12">
                    <h3>Librar.io</h3>
                </el-col>
                <el-col :lg="12">
                    <div class="userarea" v-if="!userLogged">
                        Jesteś niezalogowany
                        <el-button @click="handleLogin">Zaloguj się</el-button>
                    </div>
                    <div class="userarea" v-else>
                        Witaj, rola
                        <el-button @click="handleLogin">Twoje konto</el-button>
                    </div>
                </el-col>
            </el-row>
        </header>
    <el-menu class="el-menu-demo" mode="horizontal" @select="handleSelect">
        <el-menu-item index="admin/import" v-show="userRole==='admin'">Import danych</el-menu-item>
        <el-menu-item index="admin/export" v-show="userRole==='admin'">Export danych</el-menu-item>
        <el-submenu index="2" v-show="userRole==='admin'">
            <template slot="title">Pracownicy biblioteki</template>
            <el-menu-item index="admin/addemployee">Dodaj</el-menu-item>
            <el-menu-item index="admin/employees">Lista pracowników</el-menu-item>
        </el-submenu>
        <el-submenu index="3" v-show="userRole==='admin'||userRole==='employee'">
            <template slot="title">Baza danych</template>
            <el-submenu index="2-1">
                <template slot="title">Ksiązki</template>
                <el-menu-item index="books/list">Lista Książek</el-menu-item>
                <el-menu-item index="books">Dodaj książkę</el-menu-item>
            </el-submenu>
            <el-menu-item index="category/list" v-show="userRole==='admin'||userRole==='employee'">Kategorie</el-menu-item>
            <el-submenu index="2-4">
                <template slot="title">Autorzy</template>
                <el-menu-item index="authors/list">Lista autorów</el-menu-item>
                <el-menu-item index="authors">Dodaj autora</el-menu-item>
            </el-submenu>

        </el-submenu>
        <el-menu-item index="users" v-show="userRole==='admin'||userRole==='employee'">Użytkownicy</el-menu-item>
        <!-- poniżej elementy tylko dla zwyklego usera -->
        <el-menu-item index="youracc" v-show="userRole==='admin'||userRole==='employee'||userRole==='normal_user'">Twoje konto </el-menu-item>
        <el-menu-item index="user/reservations" v-show="userRole==='admin'||userRole==='employee'||userRole==='normal_user'">Twoje rezerwacje </el-menu-item>
        <el-menu-item index="books/list/unreserved" v-show="userRole==='admin'||userRole==='employee'||userRole==='normal_user'">Przeglądaj dostępne zasoby </el-menu-item>
    </el-menu>
        <el-drawer  ref="loginComponent"
                title="Twoje konto"
                :visible.sync="drawerVisible"
                :before-close="handleCloseDrawer">
            <login v-on:loggedSuccessful="handleLoggedUser" v-on:loggedUnSuccessful="handleNotLoggedUser" ></login>
        </el-drawer>
    </div>
</template>
<script>
    import Login from "./User/Login.vue";
    export default {
        name: 'main-menu',
        components: {Login},
        data() {
            return {
                activeIndex: '1',
                activeIndex2: '1',
                userLogged: false,
                drawerVisible: true,
            };
        },
        methods: {
            handleSelect(key, keyPath) {
                console.log(key, keyPath);
                this.$router.push("/" + key);
            },
            handleLogin(){
                console.log("login");
                this.drawerVisible = true;
            },
            handleLoggedUser(data){
                this.userLogged = true;
                console.log(data);
            },
            handleNotLoggedUser(data){
                this.userLogged = false;
                console.log(data);
            },
            handleCloseDrawer(){
                this.drawerVisible = false;
            }
        },
        mounted() {
            this.drawerVisible = false;
        },
        computed: {
            userRole(){
                return this.$store.state.userRole;
            }
        }
    }
</script>
<style lang="scss">
    .userarea{
        float: right;
    }
</style>