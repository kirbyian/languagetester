import { useEffect, useRef } from "react";
import OktaSignIn from "@okta/okta-signin-widget";
import { oktaConfig } from "../lib/oktaConfig";
import { Container } from "reactstrap";
import "./OktaSignInWidget.css";

const OktaSignInWidget = ({ onSuccess, onError }) => {
  const widgetRef = useRef();

  useEffect(() => {
    if (!widgetRef.current) {
      return false;
    }

    const widget = new OktaSignIn(oktaConfig);

    widget
      .showSignInToGetTokens({
        el: widgetRef.current,
      })
      .then(onSuccess)
      .catch(onError);

    return () => widget.remove();
  }, [onSuccess, onError]);

  return (
    <Container className="sign-in-page">
    <div className="text-center sign-in-page">
      <div ref={widgetRef}></div>
    </div>
    </Container>
  );
};

export default OktaSignInWidget;
