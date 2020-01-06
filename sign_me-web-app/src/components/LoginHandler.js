import React, {useState} from "react";
import Cookies from 'js-cookie'
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import Link from '@material-ui/core/Link';
import Grid from '@material-ui/core/Grid';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import BaseInstance from '../http-client/BaseInstance'
import {useStyles} from './LoginRegisterStyle'
import { createBrowserHistory } from "history";

function Copyright() {
    return (
        <Typography variant="body2" color="textSecondary" align="center">
            {'Copyright © '}
            <Link color="inherit">
                SignMe
        </Link>{' '}
            {new Date().getFullYear()}
            {'.'}
        </Typography>
    );
}

export default function LoginHandler ({history}) {

    if(history === undefined) {
        history = createBrowserHistory()
        history.push("/login")
    }

    console.log(Cookies.get("email"))
    if(Cookies.get("email") !== undefined)
        history.push("/home");

    Cookies.remove("currentCourse")
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [login, setLogin] = useState(true);
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const classes = useStyles();

    //if(JSON.parse(Cookies.get("email")) !== undefined)  history.push("/");

    const setInitialSession = async (data) => {
        Cookies.set("email", data.email);
        Cookies.set("firstName", data.firstName);
        Cookies.set("lastName", data.lastName);
        Cookies.set("createdCourses", data.createdCourses);
        Cookies.set("profilePicture", data.profilePicture);
        var subscribedCourses = data.followingCourses;
        subscribedCourses.sort((a, b) => (a.subject > b.subject) ? 1 : -1)
        Cookies.set("subscribedCourses", subscribedCourses);
        var res = await  BaseInstance.post("/getAllCoursesAvailable", {email: data.email});
        var allCourses = res.data
        allCourses.sort((a, b) => (a.subject > b.subject) ? 1 : -1)
        Cookies.set("allCourses", allCourses);
        console.log(JSON.parse(Cookies.get("allCourses")))
        console.log(Cookies.get())
    }

    const handleSubmit = async e => {
        var res;
        if (login) {
            res = await BaseInstance.post("login", { email: email, password: password });
            console.log("login")
            console.log(res)
            if(res.data.email !== undefined){
                setInitialSession(res.data);
                history.push("/home");
            } else {
                console.log("gestire credenziali login")
                // GESTIRE ERRORE CREDENZIALI
                history.push("/login");
            }
        } else {
            res = await BaseInstance.post("register", { firstName: firstName, lastName: lastName, email: email, password: password });
            if(res.data.email !== undefined){
                setLogin(true);
            } else {
                console.log("gestire credenziali register")
                // GESTIRE ERRORE CREDENZIALI
                history.push("/login");
            }
        }
    };

    if (login) {
        return (
            <Container component="main" maxWidth="xs">
                <CssBaseline />
                <div className={classes.paper}>
                    <img className={classes.logo} src={require('../resources/logo.png')}/>
                    <form className={classes.form} noValidate>
                        <TextField
                            variant="outlined" margin="normal" required fullWidth id="email" label="Email Address"
                            name="email" onChange={e => setEmail(e.target.value)} autoComplete="email" autoFocus />
                        <TextField
                            variant="outlined" margin="normal" required fullWidth name="password" onChange={e => setPassword(e.target.value)}
                            label="Password" type="password" id="password" autoComplete="current-password"  />
                        <Button
                            fullWidth variant="contained" color="primary" className={classes.buttonStyle} onClick={() => handleSubmit()}>
                            Login</Button>
                        <Grid container>
                            <Grid item>
                                <Link href="#" variant="body2" onClick={() => setLogin(false)}>
                                    {"Don't have an account? Register"}
                                </Link>
                            </Grid>
                        </Grid>
                    </form>
                </div>
                <Box mt={8}>
                    <Copyright />
                </Box>
            </Container>
        );
    } else {
        return (
            <Container component="main" maxWidth="xs">
                <CssBaseline />
                <div className={classes.paper}>
                    <img className={classes.logo} src={require('../resources/logo.png')}/>
                    <form className={classes.form} noValidate>
                        <Grid container spacing={2}>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    autoComplete="fname" name="firstName" variant="outlined" required fullWidth id="firstName"
                                    onChange={e => setFirstName(e.target.value)} label="First Name" autoFocus />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    variant="outlined" required fullWidth id="lastName" onChange={e => setLastName(e.target.value)}
                                    label="Last Name" name="lastName" autoComplete="lname" />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    variant="outlined" required fullWidth id="email" onChange={e => setEmail(e.target.value)}
                                    label="Email Address" name="email" autoComplete="email" />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    variant="outlined" required fullWidth name="password" label="Password"
                                    type="password" id="password" onChange={e => setPassword(e.target.value)} autoComplete="current-password"
                                    
                                />
                            </Grid>
                        </Grid>
                        <Button
                            fullWidth variant="contained" color="primary" className={classes.buttonStyle} onClick={() => handleSubmit()}>
                            Register</Button>
                        <Grid container justify="flex-end">
                            <Grid item>
                                <Link href="#" variant="body2" onClick={() => setLogin(true)}>
                                    {"Already have an account? Login"}
                                </Link>
                            </Grid>
                        </Grid>
                    </form>
                </div>
                <Box mt={5}>
                    <Copyright />
                </Box>
            </Container>
        );
    }
  };