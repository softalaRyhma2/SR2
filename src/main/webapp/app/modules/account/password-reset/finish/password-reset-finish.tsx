import React, { useState, useEffect } from 'react';
import { Col, Row, Button } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { toast } from 'react-toastify';

import { handlePasswordResetFinish, reset } from '../password-reset.reducer';
import PasswordStrengthBar from 'app/shared/layout/password/password-strength-bar';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PasswordResetFinishPage = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const [searchParams] = useSearchParams();
  const key = searchParams.get('key');
  const [isWeakPassword, setIsWeakPassword] = useState(false); // State to track weak password
  const [password, setPassword] = useState('');

  useEffect(
    () => () => {
      dispatch(reset());
    },
    [],
  );

  const handleValidSubmit = ({ newPassword }) => {
    dispatch(handlePasswordResetFinish({ key, newPassword }))
      .then(() => {
        toast.success('Password reset succesfully.');
        navigate('/');
      })
      .catch(error => {
        toast.error('Failed to reset password. Please try again.');
        console.error('Error resetting password:', error);
      });
  };

  const updatePassword = event => {
    setPassword(event.target.value);
    setIsWeakPassword(isWeak(event.target.value));
  };

  const isWeak = (passwordValue: string): boolean => {
    const strength = measureStrength(passwordValue);
    return strength < 20;
  };

  // Function to measure password strength
  const measureStrength = (passwordValue: string): number => {
    let force = 0;
    const regex = /[$-/:-?{-~!"^_`[\]]/g;
    const flags = {
      lowerLetters: /[a-z]+/.test(passwordValue),
      upperLetters: /[A-Z]+/.test(passwordValue),
      numbers: /\d+/.test(passwordValue),
      symbols: regex.test(passwordValue),
    };
    const passedMatches = Object.values(flags).filter((isMatchedFlag: boolean) => !!isMatchedFlag).length;
    force += 2 * passwordValue.length + (passwordValue.length >= 10 ? 1 : 0);
    force += passedMatches * 10;
    // penalty (short password)
    force = passwordValue.length <= 6 ? Math.min(force, 10) : force;
    // penalty (poor variety of characters)
    force = passedMatches === 1 ? Math.min(force, 10) : force;
    force = passedMatches === 2 ? Math.min(force, 20) : force;
    force = passedMatches === 3 ? Math.min(force, 40) : force;
    return force;
  };

  const getResetForm = () => {
    return (
      <ValidatedForm onSubmit={handleValidSubmit}>
        <ValidatedField
          name="newPassword"
          label={translate('global.form.newpassword.label')}
          placeholder={translate('global.form.newpassword.placeholder')}
          type="password"
          validate={{
            required: { value: true, message: translate('global.messages.validate.newpassword.required') },
            minLength: { value: 4, message: translate('global.messages.validate.newpassword.minlength') },
            maxLength: { value: 50, message: translate('global.messages.validate.newpassword.maxlength') },
            validate: v => !isWeak(v) || translate('global.messages.validate.newpassword.weak'),
          }}
          onChange={updatePassword}
          data-cy="resetPassword"
        />

        {/* Info box explaining password requirements */}
        <div className="alert alert-info" role="region">
          <ul>
            <li>{translate('global.messages.validate.newpassword.atleast4characters')}</li>
            <li>{translate('global.messages.validate.newpassword.atleast1uppercase')}</li>
            <li>{translate('global.messages.validate.newpassword.atleast1lowercase')}</li>
            <li>{translate('global.messages.validate.newpassword.atleast1number')}</li>
            <li>{translate('global.messages.validate.newpassword.atleast1specialcharacter')}</li>
            <li>{translate('global.messages.validate.newpassword.maxlength')}</li>
          </ul>
        </div>

        <PasswordStrengthBar password={password} />

        <ValidatedField
          name="confirmPassword"
          label={translate('global.form.confirmpassword.label')}
          placeholder={translate('global.form.confirmpassword.placeholder')}
          type="password"
          validate={{
            required: { value: true, message: translate('global.messages.validate.confirmpassword.required') },
            minLength: { value: 4, message: translate('global.messages.validate.confirmpassword.minlength') },
            maxLength: { value: 50, message: translate('global.messages.validate.confirmpassword.maxlength') },
            validate: v => v === password || translate('global.messages.error.dontmatch'),
          }}
          data-cy="confirmResetPassword"
        />

        <Button color="success" type="submit" data-cy="submit" disabled={isWeakPassword}>
          <Translate contentKey="reset.finish.form.button">Validate new password</Translate>
        </Button>
      </ValidatedForm>
    );
  };

  const successMessage = useAppSelector(state => state.passwordReset.successMessage);

  useEffect(() => {
    if (successMessage) {
      toast.success(translate(successMessage));
    }
  }, [successMessage]);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="4">
          <h1>
            <Translate contentKey="reset.finish.title">Reset password</Translate>
          </h1>
          <div>{key ? getResetForm() : null}</div>
        </Col>
      </Row>
    </div>
  );
};

export default PasswordResetFinishPage;
