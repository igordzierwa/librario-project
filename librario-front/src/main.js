import 'babel-polyfill';
import Vue from 'vue';

//localStorage do przechowywania info o zalogowanym użytkowniku
import VueLocalStorage from 'vue-localstorage'
Vue.use(VueLocalStorage)

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

import VueRouter from  'vue-router';
import { store } from './store';

Vue.use(ElementUI);
Vue.use(VueRouter);

import axios from 'axios';
import FileSaver from "file-saver";


import App from './App.vue'
import Home from "./Home.vue";
import Authors from "./Author/Authors.vue";
import AuthorsList from "./Author/AuthorsList.vue";
import notfound_component from "./404.vue";
import AddEmployee from "./Employee/AddEmployee.vue";
import Employees from "./Employee/Employees.vue";
import Login from "./User/Login.vue";
import BooksList from "./Book/BooksList.vue";
import CategoryList from "./Category/CategoryList.vue";
import BookListCategory from "./Category/BookListCategory.vue";
import AvailableBooks from "./Reservation/AvailableBooks.vue";
import UserLoggedReservations from "./Reservation/UserLoggedReservations.vue";
import Register from "./User/Register.vue";
import Book from "./Book/Book.vue";
import Users from "./User/Users.vue";
import Export from "./DataBase/Export.vue";
import ImportDB from "./DataBase/ImportDB.vue";
import SingleBookView from "./Book/SingleBookView.vue";
import SingleAuthorView from "./Author/SingleAuthorView.vue";
import BooksListAuthor from "./Author/BooksListAuthor.vue";
import AllUserReservations from "./Reservation/AllUserReservations.vue";
import YourAccount from "./User/YourAccount.vue";

//Vue.use(axios);

//do komunikacji między komponentami
export const bus = new Vue();

Vue.prototype.$axios = axios;
Vue.prototype.$api = "http://localhost:8081";
Vue.prototype.WindowVariable = window;
axios.defaults.withCredentials = true;

const routes = [
    { path: '/', component: Home },
    { path: '/login', component: Login },
    { path: '/authors/list',  component: AuthorsList},
    { path: '/authors/:id',  component: Authors, props: { edit: true }},
    { path: '/authors', component: Authors, props: { edit: false }},
    { path: '/books/list',  component: BooksList},
    { path: '/books/list/unreserved', component: AvailableBooks},
    { path: '/books/:id',  component: Book, props: { edit: true }},
    { path: '/books',  component: Book, props: { edit: false }},
    { path: '/user/reservations', component: UserLoggedReservations},
    { path: '/reservations/:username', component: AllUserReservations},
    { path: '/users', component: Users},
    { path: '/books/list/:category', component: BookListCategory},
    { path: '/category/list',  component: CategoryList},
    { path: '/admin/addemployee', component: AddEmployee, props:{edit:false}},
    { path: '/admin/employees', component: Employees},
    { path: '/admin/employees/:username', component: AddEmployee, props: { edit:true }},
    { path: '/admin/export', component: Export},
    { path: '/admin/import', component: ImportDB},
    { path: '/register', component: Register},
    { path: '*', component: notfound_component },
    { path: '/singlebooks/:id', component: SingleBookView },
    { path: '/singleauthor/:name', component: SingleAuthorView },
    { path: '/singleauthor/books/:name', component: BooksListAuthor },
    { path: '/youracc', component: YourAccount},
]

const router = new VueRouter({
    mode: 'history',
    routes: routes
});

Vue.mixin({
    methods: {
        flashMessengerHandle: (data, global) => {
            Object.keys(data).forEach(obj => {
                //console.log(data[obj]);
                data[obj].forEach(message => {
                    global.$message({
                        message: message,
                        dangerouslyUseHTMLString: true,
                        type: obj
                    });
                })
            })
        },
        prepareFormDataFromModel: (model) =>{
            var formData = new FormData();
            Object.keys(model).forEach(key => {
                formData.append(key, model[key])
            })
            return formData;
        },
        valid(ref){
            var valid_value = false;
            console.log(this.$refs, ref);
            this.$refs[ref].validate((valid) => {
                if (valid) {
                    valid_value = true;
                    return true;
                } else {
                    return false;
                }
            });
            return valid_value;
        },
    }

})

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app');